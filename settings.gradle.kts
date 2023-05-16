
rootProject.name = "jot-jungle"

include("application", "console", "shared", "server")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // constants
            version("jdk", "17")
            version("javafx", "18.0.2")
            version("app-group", "net.codebot")
            version("app-version", "1.0.0")

            // https://plugins.gradle.org/
            plugin("kotlin-lang", "org.jetbrains.kotlin.jvm").version("1.8.10")
            plugin("jlink", "org.beryx.jlink").version("2.26.0")
            plugin("javafx", "org.openjfx.javafxplugin").version("0.0.13")
            plugin("javamodularity", "org.javamodularity.moduleplugin").version("1.8.12")

            // https://mvnrepository.com/
            library("kotlin-coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            library("kotlin-serialization", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            library("sqlite", "org.xerial:sqlite-jdbc:3.40.1.0")
            library("exposed-core", "org.jetbrains.exposed:exposed-core:0.40.1")
            library("exposed-dao", "org.jetbrains.exposed:exposed-dao:0.40.1")
            library("exposed-jdbc", "org.jetbrains.exposed:exposed-jdbc:0.40.1")
            library("exposed-java-time", "org.jetbrains.exposed:exposed-java-time:0.40.1")
            library("junit-jupiter", "org.junit.jupiter:junit-jupiter:5.9.2")
            library("sl4j-api", "org.slf4j:slf4j-api:2.0.6")
            library("sl4j-simple", "org.slf4j:slf4j-simple:2.0.6")
            library("gluon-rich-text", "com.gluonhq:rich-text-area:1.0.0")
            library("ikonli-javafx", "org.kordamp.ikonli:ikonli-javafx:12.3.1")
            library("ikonli-lineawesome", "org.kordamp.ikonli:ikonli-lineawesome-pack:12.3.1")
            library("itextpdf", "com.itextpdf:itext7-core:7.2.3")
        }
    }
}
