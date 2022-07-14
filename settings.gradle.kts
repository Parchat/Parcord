@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

// Project Name!
rootProject.name = "Parcord"

include("paper")

enableFeaturePreview("VERSION_CATALOGS")