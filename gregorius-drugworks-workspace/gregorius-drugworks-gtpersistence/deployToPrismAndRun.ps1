param(
    [string]$PrismInstanceName = "Cleanroom-MMC-instance-0.5.4-alpha",
    [string]$PrismInstancesRoot = "C:\Users\azaza\AppData\Roaming\PrismLauncher\instances",
    [string]$PrismExecutable = "C:\Users\azaza\AppData\Local\Programs\PrismLauncher\prismlauncher.exe",
    [string]$JavaHome = "C:\Program Files\Java\jdk-25.0.2",
    [string]$GTPersistenceRuntimeSourceRoot = "",
    [string[]]$GTPersistenceRuntimeSubpaths = @("config\gregtech", "groovy", "scripts"),
    [string[]]$GTPersistenceRuntimeExcludeNames = @("persistent_data.dat"),
    [string[]]$AdditionalJarRemovalPatterns = @(),
    [int]$PostStopDelaySeconds = 2,
    [int]$PreLaunchDelaySeconds = 2,
    [int]$LaunchWaitTimeoutSeconds = 30,
    [switch]$SkipPrismLaunch,
    [switch]$SkipProcessShutdown,
    [switch]$SkipRuntimeSync,
    [switch]$SkipDeploymentVerification,
    [switch]$KeepTerminalOpen
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

function Step([string]$Message) {
    Write-Host ""
    Write-Host "==> $Message" -ForegroundColor Cyan
}

function Require-Path([string]$Path, [string]$Label) {
    if (-not (Test-Path -LiteralPath $Path)) {
        throw "$Label not found: $Path"
    }
}

function Ensure-Directory([string]$Path) {
    if (-not (Test-Path -LiteralPath $Path)) {
        New-Item -ItemType Directory -Path $Path -Force | Out-Null
    }
}

function Invoke-Gradle([string]$WorkingDir, [string]$GradleCommand, [string[]]$Arguments) {
    Push-Location $WorkingDir
    try {
        Write-Host "Running: $GradleCommand $($Arguments -join ' ')" -ForegroundColor DarkGray
        & $GradleCommand @Arguments
        if ($LASTEXITCODE -ne 0) {
            throw "Gradle exited with code $LASTEXITCODE"
        }
    }
    finally {
        Pop-Location
    }
}

function Get-PrimaryJar([string]$Directory, [string]$Label) {
    Require-Path $Directory $Label

    $jar = Get-ChildItem -LiteralPath $Directory -File -Filter "*.jar" |
        Where-Object { $_.Name -notmatch '(?i)(-dev|sources|javadoc)' } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    if ($null -eq $jar) {
        throw "No primary jar found in $Directory"
    }

    return $jar
}

function Get-FileSha256([string]$Path) {
    Require-Path $Path "File for hashing"
    return (Get-FileHash -LiteralPath $Path -Algorithm SHA256).Hash
}

function Get-GradlePropertyValue([string]$Path, [string]$Key) {
    Require-Path $Path "Gradle properties file"

    $pattern = '^\s*' + [Regex]::Escape($Key) + '\s*=\s*(.+?)\s*$'
    foreach ($line in Get-Content -LiteralPath $Path) {
        if ($line -match '^\s*#') {
            continue
        }

        $match = [Regex]::Match($line, $pattern)
        if ($match.Success) {
            return $match.Groups[1].Value.Trim()
        }
    }

    throw "Property '$Key' not found in $Path"
}

function Get-DependencyVersionFromGradleFile([string]$Path, [string]$DependencyPrefix, [string]$Label) {
    Require-Path $Path "Gradle dependency file"

    $content = Get-Content -LiteralPath $Path -Raw
    $pattern = [Regex]::Escape($DependencyPrefix) + '([^"]+)'
    $match = [Regex]::Match($content, $pattern)
    if (-not $match.Success) {
        throw "Unable to resolve $Label version from $Path"
    }

    return $match.Groups[1].Value.Trim()
}

function Get-McmodVersionFromJar([string]$JarPath, [string]$ModId) {
    Add-Type -AssemblyName System.IO.Compression.FileSystem | Out-Null

    $zip = [System.IO.Compression.ZipFile]::OpenRead($JarPath)
    try {
        $entry = $zip.Entries | Where-Object { $_.FullName -eq "mcmod.info" } | Select-Object -First 1
        if ($null -eq $entry) {
            return $null
        }

        $reader = New-Object System.IO.StreamReader($entry.Open())
        try {
            $mcmodInfo = $reader.ReadToEnd() | ConvertFrom-Json
        }
        finally {
            $reader.Dispose()
        }

        foreach ($mod in @($mcmodInfo)) {
            if ($mod.modid -eq $ModId) {
                return [string]$mod.version
            }
        }

        return $null
    }
    finally {
        $zip.Dispose()
    }
}

function Get-CurseModJarByVersion([string]$ArtifactId, [string]$ModId, [string]$MinimumVersion, [string]$Label) {
    $cacheRoot = Join-Path $env:USERPROFILE ".gradle\caches\modules-2\files-2.1\curse.maven"
    $artifactDir = Join-Path $cacheRoot $ArtifactId
    Require-Path $artifactDir "$Label CurseMaven cache directory"

    $minimum = [Version]$MinimumVersion
    $candidates = @()

    foreach ($jar in Get-ChildItem -LiteralPath $artifactDir -Recurse -File -Filter "*.jar") {
        $mcmodVersionText = Get-McmodVersionFromJar $jar.FullName $ModId
        if ([string]::IsNullOrWhiteSpace($mcmodVersionText)) {
            continue
        }

        try {
            $mcmodVersion = [Version]$mcmodVersionText
        }
        catch {
            continue
        }

        if ($mcmodVersion -lt $minimum) {
            continue
        }

        $candidates += [PSCustomObject]@{
            Jar = $jar
            Version = $mcmodVersion
            VersionText = $mcmodVersionText
        }
    }

    if ($candidates.Count -eq 0) {
        throw "No $Label runtime jar meeting version $MinimumVersion was found under $artifactDir"
    }

    return $candidates | Sort-Object Version -Descending | Select-Object -First 1
}

function Remove-MatchingJars([string]$ModsDir, [string[]]$Regexes) {
    Get-ChildItem -LiteralPath $ModsDir -File -Filter "*.jar" -ErrorAction SilentlyContinue |
        Where-Object {
            $name = $_.Name
            foreach ($regex in $Regexes) {
                if ($name -match $regex) {
                    return $true
                }
            }
            return $false
        } |
        ForEach-Object {
            Write-Host "Removing old jar: $($_.FullName)" -ForegroundColor Yellow
            Remove-Item -LiteralPath $_.FullName -Force
        }
}

function Copy-And-Verify([string]$SourcePath, [string]$DestinationDir) {
    Require-Path $SourcePath "Source jar"
    Require-Path $DestinationDir "Destination directory"

    $destinationPath = Join-Path $DestinationDir ([IO.Path]::GetFileName($SourcePath))
    Copy-Item -LiteralPath $SourcePath -Destination $destinationPath -Force

    if (-not (Test-Path -LiteralPath $destinationPath)) {
        throw "Copy failed: $destinationPath"
    }

    return $destinationPath
}

function Assert-SingleDeployedJar([string]$ModsDir, [string]$Regex, [string]$Label, [string]$ExpectedSourcePath) {
    $matches = @(Get-ChildItem -LiteralPath $ModsDir -File -Filter "*.jar" -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -match $Regex })

    if ($matches.Count -ne 1) {
        throw "Expected exactly one $Label jar in $ModsDir but found $($matches.Count)"
    }

    $expectedHash = Get-FileSha256 $ExpectedSourcePath
    $actualHash = Get-FileSha256 $matches[0].FullName
    if ($expectedHash -ne $actualHash) {
        throw "$Label jar hash mismatch. Expected $expectedHash but deployed jar has $actualHash"
    }

    return [PSCustomObject]@{
        Path = $matches[0].FullName
        Hash = $actualHash
    }
}

function Sync-DirectoryContents([string]$SourceDir, [string]$DestinationDir, [string[]]$ExcludeNames) {
    if (-not (Test-Path -LiteralPath $SourceDir)) {
        Write-Host "Skipping missing runtime path: $SourceDir" -ForegroundColor DarkYellow
        return
    }

    Ensure-Directory $DestinationDir

    Get-ChildItem -LiteralPath $DestinationDir -Force -ErrorAction SilentlyContinue |
        Where-Object { $ExcludeNames -notcontains $_.Name } |
        ForEach-Object { Remove-Item -LiteralPath $_.FullName -Recurse -Force }

    Get-ChildItem -LiteralPath $SourceDir -Force -ErrorAction SilentlyContinue |
        Where-Object { $ExcludeNames -notcontains $_.Name } |
        ForEach-Object {
            Copy-Item -LiteralPath $_.FullName -Destination (Join-Path $DestinationDir $_.Name) -Recurse -Force
        }
}

function Get-PrismManagedProcesses([string]$InstancesRoot, [string]$InstanceDir, [string]$InstanceName) {
    $instancesPattern = [Regex]::Escape($InstancesRoot)
    $instancePattern = [Regex]::Escape($InstanceDir)
    $instanceNamePattern = [Regex]::Escape($InstanceName)

    @(Get-CimInstance Win32_Process | Where-Object {
        if ($_.Name -match '^(?i)prismlauncher\.exe$') {
            return $true
        }

        if ($_.Name -notmatch '^(?i)javaw?\.exe$') {
            return $false
        }

        $cmd = [string]$_.CommandLine
        return $cmd -and (
            $cmd -match $instancePattern -or
            $cmd -match $instancesPattern -or
            $cmd -match $instanceNamePattern
        )
    })
}

function Stop-PrismEnvironment([string]$InstancesRoot, [string]$InstanceDir, [string]$InstanceName) {
    Step "Stopping Prism-managed Minecraft processes"
    $managed = @(Get-PrismManagedProcesses $InstancesRoot $InstanceDir $InstanceName)

    $javaProcesses = @($managed | Where-Object { $_.Name -match '^(?i)javaw?\.exe$' })
    $prismProcesses = @($managed | Where-Object { $_.Name -match '^(?i)prismlauncher\.exe$' })

    foreach ($process in $javaProcesses + $prismProcesses) {
        Write-Host "Stopping PID $($process.ProcessId): $($process.Name)" -ForegroundColor Yellow
        Stop-Process -Id $process.ProcessId -Force -ErrorAction SilentlyContinue
    }

    Start-Sleep -Seconds $PostStopDelaySeconds
}

function Wait-ForPrismLaunch([string]$InstancesRoot, [string]$InstanceDir, [string]$InstanceName, [int]$TimeoutSeconds) {
    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    while ((Get-Date) -lt $deadline) {
        $running = @(Get-PrismManagedProcesses $InstancesRoot $InstanceDir $InstanceName)
        if ($running.Count -gt 0) {
            return $true
        }
        Start-Sleep -Seconds 1
    }
    return $false
}

$workspace = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$addonProject = $PSScriptRoot
$commonProject = Join-Path $workspace "gregorius-drugworks-common"
$gtPersistenceProject = Join-Path $workspace "gtpersistence"

$workspaceGradle = Join-Path $workspace "gradlew.bat"
$addonGradle = Join-Path $addonProject "gradlew.bat"
$gtPersistenceGradle = Join-Path $gtPersistenceProject "gradlew.bat"

$defaultRuntimeRoot = Join-Path $gtPersistenceProject "run\client"
if (-not (Test-Path -LiteralPath $defaultRuntimeRoot)) {
    $defaultRuntimeRoot = Join-Path $gtPersistenceProject "run"
}
if ([string]::IsNullOrWhiteSpace($GTPersistenceRuntimeSourceRoot)) {
    $GTPersistenceRuntimeSourceRoot = $defaultRuntimeRoot
}

$instanceDir = Join-Path $PrismInstancesRoot $PrismInstanceName
$minecraftDir = Join-Path $instanceDir ".minecraft"
$modsDir = Join-Path $minecraftDir "mods"
$versionedModsDir = Join-Path $modsDir "1.12.2"
$jarCleanupDirs = @($modsDir)
if (Test-Path -LiteralPath $versionedModsDir) {
    $jarCleanupDirs += $versionedModsDir
}

$oldJavaHome = $env:JAVA_HOME
$oldPath = $env:Path
$scriptSucceeded = $false

try {
    Step "Checking paths"
    Require-Path $workspace "Workspace"
    Require-Path $addonProject "Gregorius Drugworks Persistence project"
    Require-Path $commonProject "gregorius-drugworks-common project"
    Require-Path $gtPersistenceProject "gtpersistence project"
    Require-Path $workspaceGradle "Workspace Gradle wrapper"
    Require-Path $addonGradle "Addon Gradle wrapper"
    Require-Path $gtPersistenceGradle "gtpersistence Gradle wrapper"
    Require-Path $JavaHome "Java home"
    Require-Path $PrismExecutable "Prism executable"
    Require-Path $instanceDir "Prism instance"

    Step "Setting Java"
    $env:JAVA_HOME = $JavaHome
    $env:Path = "$JavaHome\bin;$oldPath"
    Write-Host "JAVA_HOME = $env:JAVA_HOME" -ForegroundColor Green

    Ensure-Directory $modsDir

    if (-not $SkipProcessShutdown) {
        Stop-PrismEnvironment $PrismInstancesRoot $instanceDir $PrismInstanceName
    }

    Step "Rebuilding local jars"
    Invoke-Gradle $gtPersistenceProject $gtPersistenceGradle @("--no-daemon", "--rerun-tasks", "assemble", "publishToMavenLocal", "--console=plain")
    Invoke-Gradle $workspace $workspaceGradle @("--no-daemon", "--rerun-tasks", ":gregorius-drugworks-common:build", "--console=plain")
    Invoke-Gradle $addonProject $addonGradle @("--no-daemon", "--rerun-tasks", "assemble", "--console=plain")

    Step "Resolving built artifacts"
    $gtPersistenceJar = Get-PrimaryJar (Join-Path $gtPersistenceProject "build\libs") "gtpersistence build/libs"
    $commonJar = Get-PrimaryJar (Join-Path $commonProject "build\libs") "gregorius-drugworks-common build/libs"
    $addonJar = Get-PrimaryJar (Join-Path $addonProject "build\libs") "gregorius-drugworks-persistence build/libs"
    $gtPersistenceDependencies = Join-Path $gtPersistenceProject "dependencies.gradle"

    $modularUIVersion = Get-DependencyVersionFromGradleFile $gtPersistenceDependencies "com.cleanroommc:modularui:" "ModularUI"
    $modularUIRuntime = Get-CurseModJarByVersion "modularui-624243" "modularui" $modularUIVersion "ModularUI"

    Write-Host "gtpersistence jar:               $($gtPersistenceJar.FullName)" -ForegroundColor Green
    Write-Host "gregorius-drugworks-common jar: $($commonJar.FullName)" -ForegroundColor Green
    Write-Host "gregorius-drugworks-persistence: $($addonJar.FullName)" -ForegroundColor Green
    Write-Host "ModularUI runtime jar:          $($modularUIRuntime.Jar.FullName) (mcmod $($modularUIRuntime.VersionText))" -ForegroundColor Green

    $jarRemovalPatterns = @(
        '^(?i)(gregtech|gtceu)-.*\.jar$',
        '^(?i)gregoriusdrugworkspersistence-.*\.jar$',
        '^(?i)gregorius-drugworks-common-.*\.jar$',
        '^(?i)modularui-.*\.jar$',
        '^(?i)!?mixinbooter-.*\.jar$'
    ) + $AdditionalJarRemovalPatterns

    Step "Removing old deployed jars"
    foreach ($cleanupDir in $jarCleanupDirs) {
        Remove-MatchingJars $cleanupDir $jarRemovalPatterns
    }

    Step "Copying fresh jars into Prism"
    $copiedGtPersistence = Copy-And-Verify $gtPersistenceJar.FullName $modsDir
    $copiedCommon = Copy-And-Verify $commonJar.FullName $modsDir
    $copiedAddon = Copy-And-Verify $addonJar.FullName $modsDir
    $copiedModularUI = Copy-And-Verify $modularUIRuntime.Jar.FullName $modsDir

    Write-Host "Copied gtpersistence jar:               $copiedGtPersistence" -ForegroundColor Green
    Write-Host "Copied gregorius-drugworks-common jar: $copiedCommon" -ForegroundColor Green
    Write-Host "Copied gregorius-drugworks-persistence: $copiedAddon" -ForegroundColor Green
    Write-Host "Copied ModularUI runtime jar:          $copiedModularUI" -ForegroundColor Green

    if (-not $SkipRuntimeSync) {
        Step "Syncing gtpersistence runtime content into Prism"
        foreach ($subpath in $GTPersistenceRuntimeSubpaths) {
            $sourcePath = Join-Path $GTPersistenceRuntimeSourceRoot $subpath
            $targetPath = Join-Path $minecraftDir $subpath
            Sync-DirectoryContents $sourcePath $targetPath $GTPersistenceRuntimeExcludeNames
            Write-Host "Synced runtime path: $sourcePath -> $targetPath" -ForegroundColor Green
        }
    }

    if (-not $SkipDeploymentVerification) {
        Step "Verifying deployed Prism jars"
        $verifiedGt = Assert-SingleDeployedJar $modsDir '^(?i)(gregtech|gtceu)-.*\.jar$' "gtpersistence" $gtPersistenceJar.FullName
        $verifiedCommon = Assert-SingleDeployedJar $modsDir '^(?i)gregorius-drugworks-common-.*\.jar$' "gregorius-drugworks-common" $commonJar.FullName
        $verifiedAddon = Assert-SingleDeployedJar $modsDir '^(?i)gregoriusdrugworkspersistence-.*\.jar$' "gregorius-drugworks-persistence" $addonJar.FullName
        $verifiedModularUI = Assert-SingleDeployedJar $modsDir '^(?i)modularui-.*\.jar$' "ModularUI" $modularUIRuntime.Jar.FullName

        Write-Host "Verified gtpersistence jar:               $($verifiedGt.Path)" -ForegroundColor Green
        Write-Host "gtpersistence SHA-256:                    $($verifiedGt.Hash)" -ForegroundColor DarkGreen
        Write-Host "Verified gregorius-drugworks-common jar: $($verifiedCommon.Path)" -ForegroundColor Green
        Write-Host "gregorius-drugworks-common SHA-256:      $($verifiedCommon.Hash)" -ForegroundColor DarkGreen
        Write-Host "Verified gregorius-drugworks-persistence: $($verifiedAddon.Path)" -ForegroundColor Green
        Write-Host "gregorius-drugworks-persistence SHA-256:  $($verifiedAddon.Hash)" -ForegroundColor DarkGreen
        Write-Host "Verified ModularUI runtime jar:          $($verifiedModularUI.Path)" -ForegroundColor Green
        Write-Host "ModularUI SHA-256:                       $($verifiedModularUI.Hash)" -ForegroundColor DarkGreen
    }

    if (-not $SkipPrismLaunch) {
        Step "Launching Prism instance"
        Start-Process -FilePath $PrismExecutable -ArgumentList @("--launch", $PrismInstanceName) | Out-Null
        Start-Sleep -Seconds $PreLaunchDelaySeconds

        if (-not (Wait-ForPrismLaunch $PrismInstancesRoot $instanceDir $PrismInstanceName $LaunchWaitTimeoutSeconds)) {
            throw "Prism instance did not appear to launch within $LaunchWaitTimeoutSeconds seconds"
        }

        Write-Host "Prism instance launch detected for $PrismInstanceName" -ForegroundColor Green
    }

    Step "Done"
    $scriptSucceeded = $true
}
finally {
    $env:JAVA_HOME = $oldJavaHome
    $env:Path = $oldPath
}

if ($scriptSucceeded -and -not $SkipPrismLaunch -and -not $KeepTerminalOpen) {
    Start-Sleep -Milliseconds 750
    Stop-Process -Id $PID -Force
}
