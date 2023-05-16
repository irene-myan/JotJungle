// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import javafx.scene.control.*
import javafx.scene.layout.VBox
import kotlin.reflect.KFunction1


class ThemeDialog(updateMainTheme: KFunction1<String, Unit>) : Dialog<String>() {
    init {
        title = "Choose Theme"

        val content = VBox()
        content.spacing = 10.0

        val group = ToggleGroup()

        for (theme in ThemeManager.themeList) {
            val themeName = theme.substringBeforeLast(".")
            val button = RadioButton(themeName)
            button.toggleGroup = group
            if (theme == ThemeManager.theme) {
                button.isSelected = true
            }
            content.children.add(button)
        }

        dialogPane.content = content

        val closeButton = ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE)
        dialogPane.buttonTypes.add(closeButton)

        dialogPane.scene.stylesheets.addAll("styles.css", ThemeManager.theme)
        isResizable = true
        dialogPane.minWidth = 200.0

        group.selectedToggleProperty().addListener {_, _, _, ->
            val rb = group.selectedToggle as RadioButton
            updateMainTheme(rb.text)
            dialogPane.scene.stylesheets.clear()
            dialogPane.scene.stylesheets.addAll("styles.css", ThemeManager.theme)
        }
    }

}