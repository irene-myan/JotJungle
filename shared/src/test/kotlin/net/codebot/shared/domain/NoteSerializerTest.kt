package net.codebot.shared.domain

import com.gluonhq.richtextarea.model.DecorationModel
import com.gluonhq.richtextarea.model.Document
import com.gluonhq.richtextarea.model.ParagraphDecoration
import com.gluonhq.richtextarea.model.TextDecoration
import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class NoteSerializerTest {


    @Test
    fun basicNote() {
        val note = Note("test", 0, Document())
        val zoneId = ZoneId.systemDefault()
        val time = "1681072919"
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val noteString = Json.encodeToString(NoteSerializer, note)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":0,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"System\",\"fontFamily\":12.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"NORMAL\",\"fontWeight\":false,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":0}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[]}")
    }

    @Test
    fun noteWithBoldDecoration() {
        val decModel = DecorationModel(0, 10, TextDecoration.builder().presets().fontWeight(FontWeight.BOLD).build(), null)
        val decorationsList = listOf<DecorationModel>(decModel)
        val note = Note("test", 0, Document("2313132132312312", decorationsList, 0))
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val noteString = Json.encodeToString(NoteSerializer, note)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"2313132132312312\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":10,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"System\",\"fontFamily\":12.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"BOLD\",\"fontWeight\":false,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":-1}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[]}")
    }

    @Test
    fun noteWithItalicsDecoration() {
        val decModel = DecorationModel(0, 10, TextDecoration.builder().presets().fontPosture(FontPosture.ITALIC).build(), null)
        val decorationsList = listOf<DecorationModel>(decModel)
        val note = Note("test", 0, Document("2313132132312312", decorationsList, 0))
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val noteString = Json.encodeToString(NoteSerializer, note)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"2313132132312312\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":10,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"System\",\"fontFamily\":12.0,\"fontSize\":\"ITALIC\",\"fontPosture\":\"NORMAL\",\"fontWeight\":false,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":-1}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[]}")
    }

    @Test
    fun noteWithFontSizeDecoration() {
        val decModel = DecorationModel(0, 10, TextDecoration.builder().presets().fontSize(12.0).build(), null)
        val decorationsList = listOf<DecorationModel>(decModel)
        val note = Note("test", 0, Document("2313132132312312", decorationsList, 0))
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val noteString = Json.encodeToString(NoteSerializer, note)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"2313132132312312\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":10,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"System\",\"fontFamily\":12.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"NORMAL\",\"fontWeight\":false,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":-1}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[]}")
    }

    @Test
    fun noteWithFontFamilyDecoration() {
        val decModel = DecorationModel(0, 10, TextDecoration.builder().presets().fontFamily("Arial").build(), null)
        val decorationsList = listOf<DecorationModel>(decModel)
        val note = Note("test", 0, Document("2313132132312312", decorationsList, 0))
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val noteString = Json.encodeToString(NoteSerializer, note)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"2313132132312312\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":10,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"Arial\",\"fontFamily\":12.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"NORMAL\",\"fontWeight\":false,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":-1}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[]}")
    }

    @Test
    fun noteWithStrikethroughDecoration() {
        val decModel = DecorationModel(0, 10, TextDecoration.builder().presets().strikethrough(true).build(), null)
        val decorationsList = listOf<DecorationModel>(decModel)
        val note = Note("test", 0, Document("2313132132312312", decorationsList, 0))
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val noteString = Json.encodeToString(NoteSerializer, note)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"2313132132312312\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":10,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"System\",\"fontFamily\":12.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"NORMAL\",\"fontWeight\":true,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":-1}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[]}")
    }


    @Test
    fun noteWithUnderlineDecorations() {
        val decModel = DecorationModel(0, 10, TextDecoration.builder().presets().underline(true).build(), null)
        val decorationsList = listOf<DecorationModel>(decModel)
        val note = Note("test", 0, Document("2313132132312312", decorationsList, 0))
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val noteString = Json.encodeToString(NoteSerializer, note)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"2313132132312312\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":10,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"System\",\"fontFamily\":12.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"NORMAL\",\"fontWeight\":false,\"strikethrough\":true},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":-1}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[]}")
    }

    @Test
    fun noteWithParagraphDecorations() {
        val decModel = DecorationModel(0, 10, null, ParagraphDecoration.builder().presets().alignment(TextAlignment.CENTER).build())
        val decorationsList = listOf<DecorationModel>(decModel)
        val note = Note("test", 0, Document("2313132132312312", decorationsList, 0))
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val noteString = Json.encodeToString(NoteSerializer, note)
        println(noteString)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"2313132132312312\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":10,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"Arial\",\"fontFamily\":0.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"NORMAL\",\"fontWeight\":false,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"CENTER\",\"graphicType\":\"NONE\",\"indentationLevel\":0}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[]}")
    }

    @Test
    fun noteWithTag() {
        val note = Note("test", 0, Document())
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val tag = Tag("hello", Color.LIGHTGREEN)
        note.tags.add(tag)
        val noteString = Json.encodeToString(NoteSerializer, note)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":0,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"System\",\"fontFamily\":12.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"NORMAL\",\"fontWeight\":false,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":0}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[{\"name\":\"hello\",\"color\":{\"r\":0.5647059082984924,\"g\":0.9333333373069763,\"b\":0.5647059082984924},\"id\":-1}]}")
    }

    @Test
    fun noteWithTags() {
        val note = Note("test", 0, Document())
        val time = "1681072919"
        val zoneId = ZoneId.systemDefault()
        note.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        note.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        val tag = Tag("hello", Color.LIGHTGREEN)
        val tag2 = Tag("hello world", Color.BLACK)
        note.tags.add(tag)
        note.tags.add(tag2)
        val noteString = Json.encodeToString(NoteSerializer, note)
        println(noteString)
        assert(noteString == "{\"name\":\"test\",\"document\":{\"text\":\"\",\"caretPosition\":0,\"decoration\":[{\"start\":0,\"length\":0,\"text-decoration\":{\"foreground\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"background\":\"System\",\"fontFamily\":12.0,\"fontSize\":\"REGULAR\",\"fontPosture\":\"NORMAL\",\"fontWeight\":false,\"strikethrough\":false},\"paragraph\":{\"alignment\":\"LEFT\",\"graphicType\":\"NONE\",\"indentationLevel\":0}}]},\"id\":0,\"createDate\":{\"epochTime\":1681072919},\"editDate\":{\"epochTime\":1681072919},\"tags\":[{\"name\":\"hello\",\"color\":{\"r\":0.5647059082984924,\"g\":0.9333333373069763,\"b\":0.5647059082984924},\"id\":-1},{\"name\":\"hello world\",\"color\":{\"r\":0.0,\"g\":0.0,\"b\":0.0},\"id\":-1}]}")
    }

}