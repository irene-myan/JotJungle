// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation.tag

import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import net.codebot.application.domain.NoteList
import net.codebot.application.presentation.ThemeManager
import net.codebot.shared.domain.Note
import net.codebot.shared.domain.Tag

// Use whenever you want to apply an action to a list of tags
class TagCheckboxDialog(private var noteList: NoteList, private val currentNote: Note? = null, val showNewTagButton: Boolean = true) : Dialog<List<Tag>>() {
    private var selected: MutableList<Boolean> = MutableList(noteList.getTags().size) { false }
    init {
        val content = VBox()
        content.spacing = 10.0

        val checkboxPane = ScrollPane()
        checkboxPane.background = Background(BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))
        val checkBoxContent = VBox()
        checkBoxContent.padding = Insets(10.0)
        checkboxPane.setPrefSize(300.0, 200.0)
        checkboxPane.content = checkBoxContent
        checkBoxContent.spacing = 10.0

        // Setup the create tag dialog
        if (showNewTagButton) {
            val createTagButton = Button("Create Tag")
            createTagButton.setOnAction {
                val tagDialog = NewTagDialog()
                val res = tagDialog.showAndWait()
                res.ifPresent {
                    if (it.name.isNotEmpty()) {
                        val tag = noteList.addTag(it)
                        selected.add(false)
                        val index = selected.size - 1
                        val tagCheckbox = TagCheckbox(tag)
                        tagCheckbox.checkbox.selectedProperty().addListener{_,_,new ->
                            selected[index] = new
                        }
                        checkBoxContent.children.add(tagCheckbox)
                    }
                }
            }

            content.children.addAll(createTagButton)
        }


        if (currentNote != null) {
            val noteLabel = Label("Tags for: ${currentNote.fileName}")
            content.children.add(noteLabel)
        }

        content.children.add(checkboxPane)

        // Finish setting up the checkboxes
        for ((i, tg) in noteList.getTags().withIndex()) {

            val tagCheckbox = TagCheckbox(tg)
            tagCheckbox.checkbox.selectedProperty().addListener{_,_,new ->
                selected[i] = new
            }

            // If we are editing the tags of a current note
            // We should enable the checkboxes for that note's tags
            if (currentNote != null) {
                for (t in currentNote.tags) {
                    if (t.id == tg.id) {
                        selected[i] = true
                        tagCheckbox.checkbox.selectedProperty().set(true)
                    }
                }
            }

            checkBoxContent.children.add(tagCheckbox)
        }

        dialogPane.content = content
        dialogPane.prefWidth = 300.0
        dialogPane.prefHeight = 300.0
        this.isResizable = true

        val closeButton = ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE)
        dialogPane.buttonTypes.add(closeButton)
        dialogPane.scene.stylesheets.addAll("styles.css", ThemeManager.theme)

        this.setResultConverter {
            onSubmit(it)
        }
    }

    private fun onSubmit(bt: ButtonType) : List<Tag> {
        val tags = noteList.getTags()
        val tgList = mutableListOf<Tag>()
        if (bt.buttonData == ButtonBar.ButtonData.OK_DONE) {
            for((i, s) in selected.withIndex()) {
                if (s) {
                    tgList.add(tags[i])
                }
            }
        }
        return tgList
    }
}