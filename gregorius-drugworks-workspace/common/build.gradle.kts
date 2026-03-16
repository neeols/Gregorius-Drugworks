plugins {
    `java-library`
    `maven-publish`
}

group = "com.wurtzitane.gregoriusdrugworks"
version = "0.1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "common"
        }
    }
}