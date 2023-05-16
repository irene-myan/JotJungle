import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// https://github.com/gradle/gradle/issues/22797
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.javamodularity)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
    alias(libs.plugins.javafx)
}

group = libs.versions.app.group.get()
version = libs.versions.app.version.get()

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDirectory.set(compileKotlin.destinationDirectory)

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.sqlite)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.java.time)
    implementation(libs.kotlin.serialization)
    testImplementation(libs.junit.jupiter)
    implementation(libs.gluon.rich.text)
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    // version is determined by the plugin above
    version = libs.versions.javafx.get()
    modules = listOf("javafx.controls")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
    }
}

