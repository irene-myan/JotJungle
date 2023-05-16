// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation.tag

import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import net.codebot.application.domain.NoteList
import net.codebot.application.presentation.ThemeManager
import net.codebot.shared.domain.Tag

class EditTagDialog(private val noteList: NoteList) : Dialog<Tag> () {

    private val noteNames = ComboBox<Tag>()
    private val nameField = TextField()
    private val colorField = ColorPicker()
    init {
        val content = VBox()
        content.spacing = 10.0

        val createTagButton = Button("Create Tag")
        createTagButton.setOnAction {
            val tagDialog = NewTagDialog()
            val res = tagDialog.showAndWait()
            res.ifPresent {
                if (it.name.isNotEmpty()) {
                    noteNames.value = it
                    noteNames.items.add(it)
                    noteList.addTag(it)
                }
            }
        }
        content.children.addAll(createTagButton, Separator())

        dialogPane.scene.stylesheets.addAll("styles.css", ThemeManager.theme)

        this.title = "Edit Your Tags"

        val title = Label("Select a Tag to Edit")
        val nameTitle = Label("New Name")
        val colorTitle = Label("New Color")

        val tags = noteList.getTags()
        noteNames.items.addAll(tags)
        if (tags.isNotEmpty()) {
            noteNames.value = tags.first()
            nameField.text = tags.first().name
            colorField.value = tags.first().color
        }

        noteNames.setOnAction {
            val selectedTag = noteNames.selectionModel.selectedItem
            nameField.text = selectedTag.name
            colorField.value = selectedTag.color
        }

        content.children.addAll(title, noteNames, nameTitle, nameField, colorTitle, colorField)

        dialogPane.content = content
        dialogPane.prefWidth = 300.0
        dialogPane.prefHeight = 300.0
        this.isResizable = true

        val confirmButton = ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE)
        dialogPane.buttonTypes.add(confirmButton)

        val closeButton = ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE)
        dialogPane.buttonTypes.add(closeButton)

        this.setResultConverter {
            onSubmit(it)
        }
    }

    private fun onSubmit(bt: ButtonType) : Tag {
        if (bt.buttonData == ButtonBar.ButtonData.OK_DONE) {
            val selectedTag = noteNames.selectionModel.selectedItem
            noteList.editTag(selectedTag, nameField.text, colorField.value)
        }
        return Tag("")
    }

}