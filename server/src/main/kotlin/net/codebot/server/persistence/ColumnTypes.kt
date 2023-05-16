// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.server.persistence

import com.gluonhq.richtextarea.model.Document
import kotlinx.serialization.json.Json
import net.codebot.shared.domain.ColorSerializer
import net.codebot.shared.domain.DocumentSerializer
import net.codebot.shared.domain.LocalDateTimeSerializer
import org.jetbrains.exposed.sql.ColumnType
import javafx.scene.paint.Color
import java.sql.ResultSet
import java.time.LocalDateTime

object DocumentColumnType : ColumnType() {
    override fun sqlType(): String = "TEXT"

    override fun valueFromDB(value: Any) : Any {
        return Json.decodeFromString<Document>( DocumentSerializer, value as String)
    }

    override fun notNullValueToDB(value: Any): Any {
        try {
            return Json.encodeToString(DocumentSerializer, value as Document)
        } catch (e : Error) {
            error("Unexpected value: $value of ${value::class.simpleName}")
        }
    }

    override fun valueToString(value: Any?): String {
        return notNullValueToDB(value as Any) as String
    }

    override fun nonNullValueToString(value: Any): String {
        return notNullValueToDB(value) as String
    }

    override fun readObject(rs: ResultSet, index: Int): Any? = rs.getObject(index)
}

object LocalDateTimeColumnType : ColumnType() {
    override fun sqlType(): String = "TEXT"

    override fun valueFromDB(value: Any) : Any {
        return Json.decodeFromString<LocalDateTime>( LocalDateTimeSerializer, value as String)
    }

    override fun notNullValueToDB(value: Any): Any {
        try {
            return Json.encodeToString(LocalDateTimeSerializer, value as LocalDateTime)
        } catch (e : Error) {
            error("Unexpected value: $value of ${value::class.simpleName}")
        }
    }

    override fun valueToString(value: Any?): String {
        return notNullValueToDB(value as Any) as String
    }

    override fun nonNullValueToString(value: Any): String {
        return notNullValueToDB(value) as String
    }

    override fun readObject(rs: ResultSet, index: Int): Any? = rs.getObject(index)
}

object ColorColumnType : ColumnType() {
    override fun sqlType(): String = "TEXT"

    override fun valueFromDB(value: Any) : Any {
        return Json.decodeFromString<Color>(ColorSerializer, value as String)
    }

    override fun notNullValueToDB(value: Any): Any {
        try {
            return Json.encodeToString(ColorSerializer, value as Color)
        } catch (e : Error) {
            error("Unexpected value: $value of ${value::class.simpleName}")
        }
    }

    override fun valueToString(value: Any?): String {
        return notNullValueToDB(value as Any) as String
    }

    override fun nonNullValueToString(value: Any): String {
        return notNullValueToDB(value) as String
    }

    override fun readObject(rs: ResultSet, index: Int): Any? = rs.getObject(index)
}