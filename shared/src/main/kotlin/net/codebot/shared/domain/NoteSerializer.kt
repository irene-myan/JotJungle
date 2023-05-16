// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.shared.domain

import com.gluonhq.richtextarea.model.DecorationModel
import com.gluonhq.richtextarea.model.Document
import com.gluonhq.richtextarea.model.ParagraphDecoration
import com.gluonhq.richtextarea.model.ParagraphDecoration.GraphicType
import com.gluonhq.richtextarea.model.TextDecoration
import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object NoteSerializer : KSerializer<Note> {
    override val descriptor: SerialDescriptor
        = buildClassSerialDescriptor("note") {
            element<String>("name")
            element("document", DocumentSerializer.descriptor)
            element<Int>("id")
            element("createDate", LocalDateTimeSerializer.descriptor)
            element("editDate", LocalDateTimeSerializer.descriptor)
            element("tags", tagsSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): Note {
        val composite = decoder.beginStructure(descriptor)

        var name = ""
        var document = Document("")
        var id = 0
        var createDate = LocalDateTime.now()
        var editDate = LocalDateTime.now()
        var tags = listOf<Tag>()
        while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> name = composite.decodeStringElement(descriptor, 0)
                1 -> document = composite.decodeSerializableElement(descriptor, 1, DocumentSerializer)
                2 -> id = composite.decodeIntElement(descriptor, 2)
                3 -> createDate = composite.decodeSerializableElement(descriptor, 3, LocalDateTimeSerializer)
                4 -> editDate = composite.decodeSerializableElement(descriptor, 4, LocalDateTimeSerializer)
                5 -> tags = composite.decodeSerializableElement(descriptor, 5, tagsSerializer)
                DECODE_DONE -> break // Input is over
                else -> error("Unexpected index: $index")
            }
        }


        composite.endStructure(descriptor)
        var mutable_tags = mutableListOf<Tag>()
        mutable_tags.addAll(tags)
        val note = Note(name, id, document, createDate, editDate, mutable_tags)
        return note;
    }

    override fun serialize(encoder: Encoder, value: Note) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.fileName)
        composite.encodeSerializableElement(descriptor, 1, DocumentSerializer, value.content)
        composite.encodeIntElement(descriptor, 2, value.id)
        composite.encodeSerializableElement(descriptor, 3, LocalDateTimeSerializer, value.create_date)
        composite.encodeSerializableElement(descriptor, 4, LocalDateTimeSerializer, value.editDate)
        composite.encodeSerializableElement(descriptor, 5, tagsSerializer, value.tags)
        composite.endStructure(descriptor)
    }
}

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor
        = buildClassSerialDescriptor("date") {
            element<Long>("epochTime")
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {;
        val input = decoder.beginStructure(descriptor)
        input.decodeElementIndex(descriptor) // skip the index of the first element (we know it's at index 0)
        val epochSeconds = input.decodeLongElement(descriptor, 0)
        input.endStructure(descriptor)
        val zoneId = ZoneId.systemDefault()
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), zoneId)

    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val composite = encoder.beginStructure(descriptor)
        val zoneId = ZoneId.systemDefault()
        composite.encodeLongElement(descriptor, 0, value.atZone(zoneId).toEpochSecond())
        composite.endStructure(descriptor)
    }

}

val tagsSerializer: KSerializer<List<Tag>> = ListSerializer(TagSerialzer)

object TagSerialzer : KSerializer<Tag> {
    override val descriptor: SerialDescriptor
        = buildClassSerialDescriptor("tag") {
            element<String>("name")
            element("color", ColorSerializer.descriptor)
            element<Int>("id")
    }

    override fun deserialize(decoder: Decoder): Tag {
        val composite = decoder.beginStructure(descriptor)
        var name = ""
        var color = Color.WHITE
        var id = -1
        while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> name = composite.decodeStringElement(descriptor, 0)
                1 -> color = composite.decodeSerializableElement(descriptor, 1, ColorSerializer)
                2 -> id = composite.decodeIntElement(descriptor, 2)
                DECODE_DONE -> break // Input is over
                else -> error("Unexpected index: $index")
            }
        }

        composite.endStructure(descriptor)
        return Tag(name, color, id)
    }

    override fun serialize(encoder: Encoder, value: Tag) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.name)
        composite.encodeSerializableElement(descriptor, 1, ColorSerializer, value.color)
        composite.encodeIntElement(descriptor, 2, value.id)

        composite.endStructure(descriptor)
    }

}

object DocumentSerializer : KSerializer<Document> {
    override val descriptor: SerialDescriptor
        = buildClassSerialDescriptor("document") {
            element<String>("text")
            element<Int>("caretPosition")
            element("decoration", decorationsSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): Document {
        val composite = decoder.beginStructure(descriptor)
        var content: String = ""
        var caretPosition: Int = 0
        var decorations: List<DecorationModel>? = null

        while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> content = composite.decodeStringElement(descriptor, 0)
                1 -> caretPosition = composite.decodeIntElement(descriptor, 1)
                2 -> decorations = composite.decodeSerializableElement(descriptor, 2, decorationsSerializer)
                DECODE_DONE -> break // Input is over
                else -> error("Unexpected index: $index")
            }
        }
        composite.endStructure(descriptor)

        return Document(content, decorations, caretPosition)
    }

    override fun serialize(encoder: Encoder, value: Document) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.text)
        composite.encodeIntElement(descriptor, 1, value.caretPosition)
        composite.encodeSerializableElement(descriptor, 2, decorationsSerializer, value.decorations)
        composite.endStructure(descriptor)
    }

}

val decorationsSerializer: KSerializer<List<DecorationModel>> = ListSerializer(DecorationModelSerializer)

private object DecorationModelSerializer : KSerializer<DecorationModel> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("decoration-model") {
        element<Int>("start")
        element<Int>("length")
        element("text-decoration", TextDecorationSerializer.descriptor)
        element("paragraph", ParagraphDecorationSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): DecorationModel {
        val composite = decoder.beginStructure(descriptor)
        var start = 0
        var length = 0
        var decoration: TextDecoration? = null
        var paragraphDecoration: ParagraphDecoration? = null
        while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> start = composite.decodeIntElement(descriptor, 0)
                1 -> length = composite.decodeIntElement(descriptor, 1)
                2 -> decoration = composite.decodeSerializableElement(descriptor, 2, TextDecorationSerializer)
                3 -> paragraphDecoration = composite.decodeSerializableElement(descriptor, 3, ParagraphDecorationSerializer)
                DECODE_DONE -> break // Input is over
                else -> error("Unexpected index: $index")
            }
        }
        composite.endStructure(descriptor)
        return DecorationModel(start, length, decoration, paragraphDecoration)
    }

    override fun serialize(encoder: Encoder, value: DecorationModel) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeIntElement(descriptor, 0, value.start)
        composite.encodeIntElement(descriptor, 1, value.length)
        if (value.decoration != null) {
            composite.encodeSerializableElement(descriptor, 2, TextDecorationSerializer,
                value.decoration as TextDecoration
            )
        } else {
            composite.encodeSerializableElement(descriptor, 2, TextDecorationSerializer, TextDecoration.builder().build())
        }

        if (value.paragraphDecoration != null) {
            composite.encodeSerializableElement(descriptor, 3, ParagraphDecorationSerializer, value.paragraphDecoration)
        } else {
            composite.encodeSerializableElement(descriptor, 3, ParagraphDecorationSerializer, ParagraphDecoration.builder().build())
        }

        composite.endStructure(descriptor)
    }

}

private object TextDecorationSerializer : KSerializer<TextDecoration> {
    override val descriptor: SerialDescriptor
            = buildClassSerialDescriptor("text-decoration") {
        element("foreground", ColorSerializer.descriptor)
        element("background", ColorSerializer.descriptor)
        element<String>("fontFamily")
        element<Double>("fontSize")
        element("fontPosture", FontPostureSerializer.descriptor)
        element("fontWeight", FontWeightSerializer.descriptor)
        element<Boolean>("strikethrough")
        element<Boolean>("underline")
    }

    override fun deserialize(decoder: Decoder): TextDecoration {
        val composite = decoder.beginStructure(descriptor)
        var foreground: Color? = null
        var fontFamily: String? = null
        var fontSize: Double = 12.0
        var fontPosture: FontPosture? = null
        var fontWeight: FontWeight? = null
        var isStrikethrough:Boolean = false
        var isUnderline: Boolean = false

        while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> foreground = composite.decodeSerializableElement(descriptor, 0, ColorSerializer)
                1 -> fontFamily = composite.decodeStringElement(descriptor, 1)
                2 -> fontSize = composite.decodeDoubleElement(descriptor, 2)
                3 -> fontPosture = composite.decodeSerializableElement(descriptor, 3, FontPostureSerializer)
                4 -> fontWeight = composite.decodeSerializableElement(descriptor, 4, FontWeightSerializer)
                5 -> isStrikethrough = composite.decodeBooleanElement(descriptor, 5)
                6 -> isUnderline = composite.decodeBooleanElement(descriptor, 6)
                DECODE_DONE -> break // Input is over
                else -> error("Unexpected index: $index")
            }
        }

        composite.endStructure(descriptor)

        return TextDecoration.builder().presets()
            .foreground(foreground)
            .fontFamily(fontFamily)
            .fontSize(fontSize)
            .fontPosture(fontPosture)
            .fontWeight(fontWeight)
            .strikethrough(isStrikethrough)
            .underline(isUnderline).build()
    }

    override fun serialize(encoder: Encoder, value: TextDecoration) {
        val composite = encoder.beginStructure(descriptor)
        if (value.foreground != null) {
            composite.encodeSerializableElement(descriptor, 0, ColorSerializer, value.foreground)
        } else {
            composite.encodeSerializableElement(descriptor, 0, ColorSerializer, Color.BLACK)
        }

        if (value.fontFamily != null) {
            composite.encodeStringElement(descriptor, 1, value.fontFamily)
        } else {
            composite.encodeStringElement(descriptor, 1, "Arial")
        }

        composite.encodeDoubleElement(descriptor, 2, value.fontSize)

        if (value.fontPosture != null) {
            composite.encodeSerializableElement(descriptor, 3, FontPostureSerializer, value.fontPosture)
        } else {
            composite.encodeSerializableElement(descriptor, 3, FontPostureSerializer, FontPosture.REGULAR)
        }

        if (value.fontWeight != null) {
            composite.encodeSerializableElement(descriptor, 4, FontWeightSerializer, value.fontWeight)
        } else {
            composite.encodeSerializableElement(descriptor, 4, FontWeightSerializer, FontWeight.NORMAL)
        }

        composite.encodeBooleanElement(descriptor, 5, value.isStrikethrough)
        composite.encodeBooleanElement(descriptor, 6, value.isUnderline)
        composite.endStructure(descriptor)
    }
}

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor
            = buildClassSerialDescriptor("color") {
        element<Double>("r")
        element<Double>("g")
        element<Double>("b")
    }
    override fun deserialize(decoder: Decoder): Color {
        val composite = decoder.beginStructure(descriptor)
        var r = 0.0
        var g = 0.0
        var b = 0.0
        while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> r = composite.decodeDoubleElement(descriptor, 0)
                1 -> g = composite.decodeDoubleElement(descriptor, 1)
                2 -> b = composite.decodeDoubleElement(descriptor, 2)
                DECODE_DONE -> break // Input is over
                else -> error("Unexpected index: $index")
            }
        }
        composite.endStructure(descriptor)
        return Color(r,g,b, 1.0)
    }
    override fun serialize(encoder: Encoder, value: Color) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeDoubleElement(descriptor, 0, value.red)
        composite.encodeDoubleElement(descriptor, 1, value.green)
        composite.encodeDoubleElement(descriptor, 2, value.blue)
        composite.endStructure(descriptor)
    }
}

private object FontPostureSerializer : KSerializer<FontPosture> {
    override val descriptor: SerialDescriptor
            = PrimitiveSerialDescriptor("fontPosture", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): FontPosture {
        val string = decoder.decodeString()
        return if (string == FontPosture.REGULAR.toString()) {
            FontPosture.REGULAR
        } else {
            FontPosture.ITALIC
        }
    }
    override fun serialize(encoder: Encoder, value: FontPosture) {
        encoder.encodeString(value.toString())
    }
}

private object FontWeightSerializer : KSerializer<FontWeight> {
    override val descriptor: SerialDescriptor
            = PrimitiveSerialDescriptor("fontPosture", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): FontWeight {
        val string = decoder.decodeString()
        return if (string == FontWeight.NORMAL.toString()) {
            FontWeight.NORMAL
        } else {
            FontWeight.BOLD
        }
    }
    override fun serialize(encoder: Encoder, value: FontWeight) {
        encoder.encodeString(value.toString())
    }
}

private object ParagraphDecorationSerializer : KSerializer<ParagraphDecoration> {
    override val descriptor: SerialDescriptor
            = buildClassSerialDescriptor("paragraph") {
        element("alignment", TextAlignmentSerializer.descriptor)
        element("graphicType", TextDecorationSerializer.descriptor)
        element<Int>("indentationLevel")
    }

    override fun deserialize(decoder: Decoder): ParagraphDecoration {
        val composite = decoder.beginStructure(descriptor)

        var alignment: TextAlignment? = null
        var graphicType: GraphicType? = null
        var indentLevel = 0

        while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> alignment = composite.decodeSerializableElement(descriptor, 0, TextAlignmentSerializer)
                1 -> graphicType = composite.decodeSerializableElement(descriptor, 1, GraphicTypeSerializer)
                2 -> indentLevel = composite.decodeIntElement(descriptor, 2)
                DECODE_DONE -> break // Input is over
                else -> error("Unexpected index: $index")
            }
        }

        composite.endStructure(descriptor)
        return ParagraphDecoration.builder().presets()
            .alignment(alignment)
            .graphicType(graphicType)
            .indentationLevel(indentLevel)
            .build()
    }

    override fun serialize(encoder: Encoder, value: ParagraphDecoration) {
        val composite = encoder.beginStructure(descriptor)
        if (value.alignment != null) {
            composite.encodeSerializableElement(descriptor, 0, TextAlignmentSerializer, value.alignment)
        } else {
            composite.encodeSerializableElement(descriptor, 0, TextAlignmentSerializer, TextAlignment.LEFT)
        }

        if (value.graphicType != null) {
            composite.encodeSerializableElement(descriptor, 1, GraphicTypeSerializer, value.graphicType)
        } else {
            composite.encodeSerializableElement(descriptor, 1, GraphicTypeSerializer, GraphicType.NONE)
        }

        composite.encodeIntElement(descriptor, 2, value.indentationLevel)

        composite.endStructure(descriptor)
    }
}

private object TextAlignmentSerializer : KSerializer<TextAlignment> {
    override val descriptor: SerialDescriptor
            = PrimitiveSerialDescriptor("text-align", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): TextAlignment {
        val string = decoder.decodeString()
        if (string == TextAlignment.CENTER.toString()) {
            return TextAlignment.CENTER
        } else if (string == TextAlignment.RIGHT.toString()) {
            return TextAlignment.RIGHT
        } else {
            return TextAlignment.LEFT
        }
    }

    override fun serialize(encoder: Encoder, value: TextAlignment) {
        encoder.encodeString(value.toString())
    }

}

private object GraphicTypeSerializer : KSerializer<GraphicType> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("graphic-type", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): GraphicType {
        val string = decoder.decodeString()
        if (string == GraphicType.BULLETED_LIST.toString()) {
            return GraphicType.BULLETED_LIST
        } else if (string == GraphicType.NUMBERED_LIST.toString()) {
            return GraphicType.NUMBERED_LIST
        } else {
            return GraphicType.NONE
        }
    }

    override fun serialize(encoder: Encoder, value: GraphicType) {
        encoder.encodeString(value.toString())
    }
}
