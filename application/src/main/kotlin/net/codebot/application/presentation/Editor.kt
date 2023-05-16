// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import com.gluonhq.richtextarea.RichTextArea
import com.gluonhq.richtextarea.action.Action
import com.gluonhq.richtextarea.action.ParagraphDecorateAction
import com.gluonhq.richtextarea.action.TextDecorateAction
import com.gluonhq.richtextarea.model.Document
import com.gluonhq.richtextarea.model.ParagraphDecoration
import com.gluonhq.richtextarea.model.TextDecoration
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import javafx.util.StringConverter
import net.codebot.shared.domain.Note
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.lineawesome.LineAwesomeSolid

// Code Adapted from https://github.com/gluonhq/rich-text-area
class Editor() : VBox() {
    private val editor = RichTextArea()
    private val editorArea = StackPane(editor)

    private val toolbar = ToolBar()
    private val secondaryToolbar = ToolBar()
    private val toolBox = VBox(toolbar, secondaryToolbar)

    private val cutButton = actionButton(LineAwesomeSolid.CUT, editor.actionFactory.cut())
    private val copyButton = actionButton(LineAwesomeSolid.COPY, editor.actionFactory.copy())
    private val pasteButton = actionButton(LineAwesomeSolid.PASTE, editor.actionFactory.paste())
    private val undoButton = actionButton(LineAwesomeSolid.UNDO, editor.actionFactory.undo())
    private val redoButton = actionButton(LineAwesomeSolid.REDO, editor.actionFactory.redo())

    private val boldButton = createGraphicToggleButton(LineAwesomeSolid.BOLD)
    private val italicsButton = createGraphicToggleButton(LineAwesomeSolid.ITALIC)
    private val underlineButton = createGraphicToggleButton(LineAwesomeSolid.UNDERLINE)
    private val strikethroughButton = createGraphicToggleButton(LineAwesomeSolid.STRIKETHROUGH)

    private val alignLeftButton = createGraphicToggleButton(LineAwesomeSolid.ALIGN_LEFT)
    private val alignCenterButton = createGraphicToggleButton(LineAwesomeSolid.ALIGN_CENTER)
    private val alignRightButton = createGraphicToggleButton(LineAwesomeSolid.ALIGN_RIGHT)
    private val orderedListButton = createGraphicToggleButton(LineAwesomeSolid.LIST_OL)
    private val unorderedListButton = createGraphicToggleButton(LineAwesomeSolid.LIST_UL)

    private val fonts = ComboBox<String>()
    private val fontColor = ColorPicker()
    private val fontSize = ComboBox<Double>()

    init {
        editorArea.alignment = Pos.CENTER
        StackPane.setAlignment(editor, Pos.BOTTOM_CENTER)
        StackPane.setMargin(editor, Insets(10.0))

        this.children.addAll(toolBox, editorArea)
        this.spacing = 10.0
        setVgrow(editor, Priority.ALWAYS)
        setVgrow(editorArea, Priority.ALWAYS)

        editor.isAutoSave = true
        editor.styleClass.add("editor")

        editorArea.styleClass.add("editor-area")

        setTooltips()
    }

    // Bind the buttons to the editor
    init {
        // BOLD
        TextDecorateAction(editor, boldButton.selectedProperty().asObject(),
            {td: TextDecoration -> td.fontWeight == FontWeight.BOLD},
            {builder: TextDecoration.Builder, property: Boolean ->
                builder.fontWeight(
                    if (property) FontWeight.BOLD else FontWeight.NORMAL
                ).build()
            })

        // ITALICS
        TextDecorateAction(editor, italicsButton.selectedProperty().asObject(),
            {td: TextDecoration -> td.fontPosture == FontPosture.ITALIC},
            {builder: TextDecoration.Builder, property: Boolean ->
                builder.fontPosture(
                    if (property) FontPosture.ITALIC else FontPosture.REGULAR
                ).build()
            })

        // UNDERLINE
        TextDecorateAction(editor, underlineButton.selectedProperty().asObject(),
            {td: TextDecoration -> td.isUnderline},
            {builder: TextDecoration.Builder, property: Boolean ->
                builder.underline(
                    property
                ).build()
            })

        // STRIKETHROUGH
        TextDecorateAction(editor, strikethroughButton.selectedProperty().asObject(),
            {td: TextDecoration -> td.isStrikethrough},
            {builder: TextDecoration.Builder, property: Boolean ->
                builder.strikethrough(
                    property
                ).build()
            })

        // FONT COLOR
        TextDecorateAction(editor, fontColor.valueProperty(),
            { obj: TextDecoration -> obj.foreground },
            { builder: TextDecoration.Builder, property: Color? ->
                builder.foreground(
                    property
                ).build()
            })
        fontColor.value = Color.BLACK

        // FONTS
        fonts.items.addAll(Font.getFamilies())
        fonts.value = "Arial"
        fonts.prefWidth = 125.0
        TextDecorateAction(editor, fonts.valueProperty(),
            { obj: TextDecoration -> obj.fontFamily },
            { builder: TextDecoration.Builder, property: String ->
                builder.fontFamily(
                    property
                ).build()
            })

        // FONT SIZE
        fontSize.isEditable = true
        fontSize.items.addAll(5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 14.0, 16.0, 18.0, 22.0, 24.0, 26.0, 28.0, 36.0, 48.0, 72.0)
        TextDecorateAction(editor, fontSize.valueProperty(),
            { td: TextDecoration -> td.fontSize },
            { builder: TextDecoration.Builder, a: Double ->
                builder.fontSize(
                    a
                ).build()
            })
        fontSize.value = 12.0
        fontSize.prefWidth = 80.0
        fontSize.converter = object : StringConverter<Double>() {
            override fun toString(d: Double): String {
                return d.toInt().toString()
            }
            override fun fromString(s: String): Double {
                return s.toDouble()
            }
        }

        // ALIGN LEFT
        ParagraphDecorateAction(editor, alignLeftButton.selectedProperty().asObject(),
            { pd: ParagraphDecoration -> pd.alignment == TextAlignment.LEFT },
            { builder: ParagraphDecoration.Builder, _: Boolean? ->
                builder.alignment(
                    TextAlignment.LEFT
                ).build()
            })

        // ALIGN CENTER
        ParagraphDecorateAction(editor, alignCenterButton.selectedProperty().asObject(),
            { pd: ParagraphDecoration -> pd.alignment == TextAlignment.CENTER },
            { builder: ParagraphDecoration.Builder, _: Boolean? ->
                builder.alignment(
                    TextAlignment.CENTER
                ).build()
            })

        // ALIGN RIGHT
        ParagraphDecorateAction(editor, alignRightButton.selectedProperty().asObject(),
            { pd: ParagraphDecoration -> pd.alignment == TextAlignment.RIGHT },
            { builder: ParagraphDecoration.Builder, _: Boolean? ->
                builder.alignment(
                    TextAlignment.RIGHT
                ).build()
            })

        // ORDERED LIST
        ParagraphDecorateAction(editor, orderedListButton.selectedProperty().asObject(),
            { pd: ParagraphDecoration -> pd.graphicType == ParagraphDecoration.GraphicType.NUMBERED_LIST},
            { builder: ParagraphDecoration.Builder, a: Boolean ->
                builder.graphicType(
                    if (a) ParagraphDecoration.GraphicType.NUMBERED_LIST
                    else ParagraphDecoration.GraphicType.NONE
                ).build()
            })

        // UNORDERED LIST
        ParagraphDecorateAction(editor, unorderedListButton.selectedProperty().asObject(),
            { pd: ParagraphDecoration -> pd.graphicType == ParagraphDecoration.GraphicType.BULLETED_LIST},
            { builder: ParagraphDecoration.Builder, a: Boolean ->
                builder.graphicType(
                    if (a) ParagraphDecoration.GraphicType.BULLETED_LIST
                    else ParagraphDecoration.GraphicType.NONE
                ).build()
            })


    }
    init {
        toolbar.items.setAll(
            cutButton,
            copyButton,
            pasteButton,
            Separator(Orientation.VERTICAL),
            undoButton,
            redoButton,
            Separator(Orientation.VERTICAL),
            boldButton,
            italicsButton,
            underlineButton,
            strikethroughButton,
            fontColor,
        )

        secondaryToolbar.items.setAll(
            fonts,
            fontSize,
            Separator(Orientation.VERTICAL),
            alignLeftButton,
            alignCenterButton,
            alignRightButton,
            orderedListButton,
            unorderedListButton
        )
    }

    private fun actionButton(ikon: Ikon, action: Action): Button {
        val button = Button()
        val icon = FontIcon(ikon)
        icon.iconSize = 20
        button.graphic = icon
        button.disableProperty().bind(action.disabledProperty())
        button.onAction = EventHandler { event: ActionEvent? ->
            action.execute(event)
        }
        return button
    }

    private fun createGraphicToggleButton(
        ikon: Ikon,
    ): ToggleButton {
        val toggleButton = ToggleButton()
        val icon = FontIcon(ikon)
        icon.iconSize = 20
        toggleButton.graphic = icon
        return toggleButton
    }

    fun undo(event: ActionEvent?) {
        editor.actionFactory.undo().execute(event)
    }
    fun redo(event: ActionEvent?) {
        editor.actionFactory.redo().execute(event)
    }
    fun cut(event: ActionEvent?) {
        editor.actionFactory.cut().execute(event)
    }
    fun copy(event: ActionEvent?) {
        editor.actionFactory.copy().execute(event)
    }
    fun paste(event: ActionEvent?) {
        editor.actionFactory.paste().execute(event)
    }

    fun setContent(document: Document) {
        editor.document = document;
    }
    fun getContent() : Document {
        return editor.document;
    }
    fun hideToolbox() {
        this.children.remove(toolBox)
    }

    fun showToolbox() {
        this.children.add(0, toolBox)
    }

    private fun setTooltips() {
        cutButton.tooltip = Tooltip("Cut")
        copyButton.tooltip = Tooltip("Copy")
        pasteButton.tooltip = Tooltip("Paste")

        undoButton.tooltip = Tooltip("Undo")
        redoButton.tooltip = Tooltip("Redo")

        boldButton.tooltip = Tooltip("Bold")
        italicsButton.tooltip = Tooltip("Italic")
        underlineButton.tooltip = Tooltip("Underline")
        strikethroughButton.tooltip = Tooltip("Strikethrough")
        fontColor.tooltip = Tooltip("Font color")

        fonts.tooltip = Tooltip("Font")
        fontSize.tooltip = Tooltip("Font size")
        alignLeftButton.tooltip = Tooltip("Align left")
        alignCenterButton.tooltip = Tooltip("Align center")
        alignRightButton.tooltip = Tooltip("Align right")
        orderedListButton.tooltip = Tooltip("Numbering")
        unorderedListButton.tooltip = Tooltip("Bullets")
    }
}