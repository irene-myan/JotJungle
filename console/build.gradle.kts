import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// https://github.com/gradle/gradle/issues/22797
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.javamodularity)
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
    implementation(project(":shared"))
    testImplementation(libs.junit.jupiter)
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
    mainModule.set("console")
    mainClass.set("net.codebot.console.MainKt")
}