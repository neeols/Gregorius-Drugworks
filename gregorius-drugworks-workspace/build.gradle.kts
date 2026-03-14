plugins {
    base
}

allprojects {
    group = "com.azarkh.gregoriusdrugworks"
    version = "0.1.0"

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net")
        maven("https://maven.cleanroommc.com")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.blamejared.com")
    }
}