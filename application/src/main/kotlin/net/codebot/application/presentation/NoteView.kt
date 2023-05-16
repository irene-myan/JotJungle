// Copyright (c) Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import net.codebot.shared.domain.Note
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.lineawesome.LineAwesomeSolid
import java.util.*
import kotlin.concurrent.timerTask

class NoteView(note: Note) : VBox() {
    val note = note
    val noteTitle = TextField(note.fileName)
    val editor = Editor()
    private val backButton = Button()
    private val toggleToolButton = Button()

    init {
        noteTitle.font = Font.font("Arial", FontWeight.BOLD, 18.0)
        noteTitle.styleClass.add("note-title")
        noteTitle.focusedProperty().addListener{_,_,_ ->
            note.fileName = noteTitle.text
        }

        setTooltips()

        setVgrow(editor, Priority.ALWAYS)

        editor.setContent(note.content)

        val backIcon = FontIcon(LineAwesomeSolid.ARROW_LEFT)
        backIcon.iconSize = 20
        backButton.graphic = backIcon

        val titleContainer = HBox(noteTitle)
        titleContainer.alignment = Pos.CENTER

        val sep = Separator()
        sep.orientation = Orientation.VERTICAL

        val topBar = HBox()
        topBar.padding = Insets(5.0, 5.0, 10.0, 5.0)
        topBar.children.addAll(backButton, sep, titleContainer)
        topBar.spacing = 5.0
        topBar.alignment = Pos.CENTER_LEFT

        val angleUp = FontIcon(LineAwesomeSolid.ANGLE_UP)
        val angleDown = FontIcon(LineAwesomeSolid.ANGLE_DOWN)
        toggleToolButton.graphic = angleDown
        toggleToolButton.setOnAction { _ ->
            if (toggleToolButton.graphic == angleUp) {
                toggleToolButton.graphic = angleDown
                editor.showToolbox()
                toggleToolButton.tooltip.text = "Hide tools"
            } else {
                toggleToolButton.graphic = angleUp
                editor.hideToolbox()
                toggleToolButton.tooltip.text = "Show tools"
            }
        }

        val spacer = Pane()
        HBox.setHgrow(spacer, Priority.SOMETIMES)
        topBar.children.addAll(spacer, toggleToolButton)

        this.maxWidth = Double.MAX_VALUE
        this.padding = Insets(5.0)
        this.children.addAll(topBar, editor)

        val saveAlert = Alert(Alert.AlertType.CONFIRMATION)
        saveAlert.headerText = "Exit note"
        backButton.setOnAction {_ ->
            saveNote()
            fireEvent(LayoutEvent(LayoutEvent.LIST))
        }

    }

    fun saveNote() {
        note.save_note(editor.getContent())
    }

    fun focusTitle() {
        Timer().schedule(timerTask {
            Platform.runLater{
                while(!noteTitle.isFocused) {
                    noteTitle.requestFocus()
                }
            }
        }, 50)
    }

    private fun setTooltips() {
        backButton.tooltip = Tooltip("Back")
        toggleToolButton.tooltip = Tooltip("Hide tools")
    }
}
