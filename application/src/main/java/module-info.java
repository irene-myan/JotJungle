module application {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires javafx.media;
    requires kotlinx.coroutines.core.jvm;
    requires rich.text.area;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.lineawesome;
    requires shared;
    requires java.net.http;
    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;
    requires exposed.core;
    requires exposed.java.time;
    requires kernel;
    requires layout;
    exports net.codebot.application;
    exports net.codebot.application.domain;
}