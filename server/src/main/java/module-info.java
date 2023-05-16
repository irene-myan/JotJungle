module net.codebot.server {
    requires kotlin.stdlib;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires shared;
    requires spring.web;
    requires exposed.core;
    requires exposed.java.time;
    requires rich.text.area;
    requires kotlinx.serialization.json;
    requires java.sql;
    requires javafx.graphics;
    exports net.codebot.server;
}
