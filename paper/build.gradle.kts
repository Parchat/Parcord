plugins {
    id("parcord.paper-build")
    id("parcord.parent-build")

    id("parcord.jda-build")

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    // Paper API
    maven("https://repo.papermc.io/repository/maven-public/")

    mavenLocal()
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.0.0")

    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")

    compileOnly("com.google.inject:guice:5.1.0")

    paperDevBundle("1.19-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        relocate("org.bstats", "net.parchat.libs.bstats")

        archiveFileName.set("${rootProject.name}-v${project.version}.jar")
    }

    reobfJar {}

    assemble {
        dependsOn(reobfJar)
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "version" to project.version,
                "description" to project.description
            )
        }
    }
}