import gradle.kotlin.dsl.accessors._72efc76fad8c8cf3476d335fb6323bde.compileJava
import gradle.kotlin.dsl.accessors._72efc76fad8c8cf3476d335fb6323bde.java
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.java
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

plugins {
    java
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("commons-validator:commons-validator:1.7")

    compileOnly("ch.qos.logback:logback-classic:1.2.11")

    compileOnly("net.dv8tion:JDA:5.0.0-alpha.13")

    compileOnly("commons-cli:commons-cli:1.5.0")
}