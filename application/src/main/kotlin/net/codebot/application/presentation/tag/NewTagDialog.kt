// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation.tag

import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import net.codebot.application.presentation.ThemeManager
import net.codebot.shared.domain.Tag

class NewTagDialog : Dialog<Tag>() {

    val nameField = TextField("")
    val colorField = ColorPicker(Color.LIGHTGREEN)
    init {
        title = "Create a Tag"

        val nameTitle = Label("Tag Name")
        val colorTitle = Label("Tag Color")

        val content = VBox()
        content.spacing = 10.0
        content.children.addAll(nameTitle, nameField, colorTitle, colorField)

        dialogPane.content = content

        val closeButton = ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE)
        dialogPane.buttonTypes.add(closeButton)
        dialogPane.scene.stylesheets.addAll("styles.css", ThemeManager.theme)

        val confirmButton = ButtonType("Create", ButtonBar.ButtonData.OK_DONE)

        dialogPane.buttonTypes.add(confirmButton)

        setResultConverter { onSubmit(it) }
    }

    private fun onSubmit(bt: ButtonType) : Tag? {
        var tg: Tag? = null
        if (bt.buttonData == ButtonBar.ButtonData.OK_DONE) {
            tg = Tag(nameField.text, colorField.value);
        }

        return tg
    }

}