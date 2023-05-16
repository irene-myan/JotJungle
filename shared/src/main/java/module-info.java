module shared {
    requires kotlin.stdlib;
    requires rich.text.area;
    requires org.xerial.sqlitejdbc;
    requires exposed.core;
    requires exposed.java.time;
    requires kotlinx.serialization.json;
    requires javafx.graphics;
    requires java.net.http;
    exports net.codebot.shared.domain;
}