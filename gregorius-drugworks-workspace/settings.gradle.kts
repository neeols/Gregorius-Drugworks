rootProject.name = "gregorius-drugworks-workspace"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8.3"
}

include("gregorius-drugworks-common")
