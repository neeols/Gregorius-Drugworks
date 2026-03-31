plugins {
    base
}

allprojects {
    group = "com.wurtzitane.gregoriusdrugworks"
    version = "0.1.0"

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.cleanroommc.com")
        maven("https://maven.blamejared.com")
        maven("https://maven.outlands.top/releases")
    }
}