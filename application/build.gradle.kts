import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// https://github.com/gradle/gradle/issues/22797
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
//    kotlin("jvm") version "1.6.20"
//    id("org.openjfx.javafxplugin") version "0.0.13"
//    id("org.beryx.jlink") version "2.25.0"
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
    distribution
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.javamodularity)
    alias(libs.plugins.javafx)
    alias(libs.plugins.jlink)
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
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test"))
    implementation("com.gluonhq:rich-text-area:1.0.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.kordamp.ikonli:ikonli-lineawesome-pack:12.3.1")
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization)
    testImplementation(libs.junit.jupiter)
    implementation(libs.gluon.rich.text)
    implementation(libs.ikonli.javafx)
    implementation(libs.ikonli.lineawesome)
    implementation(libs.itextpdf)

    //data/db libs
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.java.time)
    implementation(libs.kotlin.serialization)
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
    }
}

application {
    mainModule.set("application")
    mainClass.set("net.codebot.application.Main")
}


javafx {
    // version is determined by the plugin above
    version = libs.versions.javafx.get()
    modules = listOf("javafx.controls", "javafx.media")
}

// https://stackoverflow.com/questions/74453018/jlink-package-kotlin-in-both-merged-module-and-kotlin-stdlib
jlink {
    forceMerge("kotlin")
}