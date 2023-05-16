import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.javamodularity)
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("plugin.spring") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
    alias(libs.plugins.javafx)

}

group = libs.versions.app.group.get()
version = libs.versions.app.version.get()
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(kotlin("reflect"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation(project(":shared"))
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.java.time)
    implementation(libs.kotlin.serialization)
    implementation(libs.gluon.rich.text)
    implementation(libs.sqlite)
    testImplementation(libs.junit.jupiter)

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
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