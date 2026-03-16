$ErrorActionPreference = "Stop"

# =========================
# CONFIG
# =========================
$workspace   = "C:\Users\azaza\Gregorius-Drugworks\gregorius-drugworks-workspace"
$project     = Join-Path $workspace "gtpersistence"
$gradleBat   = Join-Path $workspace "gradlew.bat"

$javaHome    = "C:\Program Files\Java\jdk-25.0.2"

$prismExe    = "C:\Users\azaza\AppData\Local\Programs\PrismLauncher\prismlauncher.exe"
$instanceDir = "C:\Users\azaza\AppData\Roaming\PrismLauncher\instances\Cleanroom-MMC-instance-0.5.4-alpha"
$modsDir     = Join-Path $instanceDir ".minecraft\mods"

$publishCommon   = $true
$launchAfterCopy = $true

# =========================
# HELPERS
# =========================
function Step($msg) {
    Write-Host ""
    Write-Host "==> $msg" -ForegroundColor Cyan
}

function Fail($msg) {
    Write-Host ""
    Write-Host "DEPLOY FAILED: $msg" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

function Require-Path($path, $label) {
    if (-not (Test-Path -LiteralPath $path)) {
        Fail "$label not found: $path"
    }
}

function Run-Gradle($workingDir, [string[]]$args) {
    Push-Location $workingDir
    try {
        Write-Host "Running: $gradleBat $($args -join ' ')" -ForegroundColor DarkGray
        & $gradleBat @args
        if ($LASTEXITCODE -ne 0) {
            Fail "Gradle exited with code $LASTEXITCODE"
        }
    }
    finally {
        Pop-Location
    }
}

function Get-BuiltJar($libsDir) {
    Require-Path $libsDir "build/libs"

    $jar = Get-ChildItem -LiteralPath $libsDir -File -Filter "*.jar" |
            Where-Object { $_.Name -notmatch '(?i)(sources|javadoc)' } |
            Sort-Object LastWriteTime -Descending |
            Select-Object -First 1

    if (-not $jar) {
        Fail "No built jar found in $libsDir"
    }

    return $jar
}

# =========================
# MAIN
# =========================
$oldJavaHome = $env:JAVA_HOME
$oldPath = $env:Path

try {
    Step "Checking paths"
    Require-Path $workspace "Workspace"
    Require-Path $project "gtpersistence project"
    Require-Path $gradleBat "Gradle wrapper"
    Require-Path $javaHome "Java home"
    Require-Path $prismExe "Prism executable"
    Require-Path $instanceDir "Prism instance"

    Step "Setting Java"
    $env:JAVA_HOME = $javaHome
    $env:Path = "$javaHome\bin;$oldPath"
    Write-Host "JAVA_HOME = $env:JAVA_HOME" -ForegroundColor Green

    if (-not (Test-Path -LiteralPath $modsDir)) {
        Step "Creating mods directory"
        New-Item -ItemType Directory -Path $modsDir -Force | Out-Null
    }

    if ($publishCommon) {
        Step "Publishing common to mavenLocal"
        Run-Gradle $workspace @(":common:publishToMavenLocal")
    }

    Step "Building gtpersistence"
    Run-Gradle $project @(":gtpersistence:clean", ":gtpersistence:assemble")

    Step "Finding built jar"
    $libsDir = Join-Path $project "build\libs"
    $jar = Get-BuiltJar $libsDir
    Write-Host "Built jar: $($jar.FullName)" -ForegroundColor Green

    Step "Stopping Prism if running"
    Get-Process PrismLauncher -ErrorAction SilentlyContinue | Stop-Process -Force
    Start-Sleep -Seconds 2

    Step "Removing old gtpersistence jars from instance"
    Get-ChildItem -LiteralPath $modsDir -File -Filter "*.jar" -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -match '(?i)gregorius.*persistence|gtpersistence' } |
            ForEach-Object {
                Write-Host "Removing old jar: $($_.FullName)" -ForegroundColor Yellow
                Remove-Item -LiteralPath $_.FullName -Force
            }

    Step "Copying new jar"
    $destination = Join-Path $modsDir $jar.Name
    Copy-Item -LiteralPath $jar.FullName -Destination $destination -Force

    if (-not (Test-Path -LiteralPath $destination)) {
        Fail "Jar was not copied to $destination"
    }

    Write-Host "Copied to: $destination" -ForegroundColor Green

    Step "Current mods folder"
    Get-ChildItem -LiteralPath $modsDir -File |
            Sort-Object LastWriteTime -Descending |
            Select-Object LastWriteTime, Length, Name |
            Format-Table -AutoSize

    if ($launchAfterCopy) {
        Step "Launching Prism"
        Start-Process -FilePath $prismExe
        Start-Sleep -Seconds 3
        & $prismExe --launch "Cleanroom-MMC-instance-0.5.4-alpha"
    }

    Step "Done"
    Read-Host "Press Enter to exit"
}
catch {
    Write-Host ""
    Write-Host $_ | Out-String -ForegroundColor Red
    Read-Host "Press Enter to exit"
}
finally {
    $env:JAVA_HOME = $oldJavaHome
    $env:Path = $oldPath
}