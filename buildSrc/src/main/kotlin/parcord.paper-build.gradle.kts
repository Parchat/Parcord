plugins {
    // Paper-weight
    id("io.papermc.paperweight.userdev")
}

group = "net.parcord.parcod"
version = "1.0.0-${System.getenv("BUILD_NUMBER") ?: "SNAPSHOT"}"
description = "A paper impl of Parcord."

repositories {
    mavenCentral()
}