// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination

class AppMenu : MenuBar() {
    val fileMenu = Menu("File")
    val saveItem = MenuItem("Save")
    val quitItem = MenuItem("Quit")
    val newItem = MenuItem("New")
    val exportItem = MenuItem("Export")

    val editMenu = Menu("Edit")
    val undoItem = MenuItem("Undo")
    val redoItem = MenuItem("Redo")
    val copyItem = MenuItem("Copy")
    val pasteItem = MenuItem("Paste")
    val cutItem = MenuItem("Cut")
    val deleteTags = MenuItem("Delete Your Tags")
    val editTags = MenuItem("Edit Your Tags")

    val viewMenu = Menu("View")
    val minimizeItem = MenuItem("Minimize")
    val maximizeItem = MenuItem("Maximize")
    val themeItem = MenuItem("Change Theme")
    val oogaItem = MenuItem("Ooga Mode")

    init {
        // File
        fileMenu.items.add(newItem)
        newItem.accelerator = KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN)

        fileMenu.items.add(saveItem)
        saveItem.accelerator = KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)

        fileMenu.items.add(quitItem)
        quitItem.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN)

        fileMenu.items.add(exportItem)
        exportItem.accelerator = KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN)

        // Edit
        editMenu.items.add(undoItem)
        undoItem.accelerator = KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN)

        editMenu.items.add(redoItem)
        redoItem.accelerator = KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN)

        editMenu.items.add(copyItem)
        copyItem.accelerator = KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)

        editMenu.items.add(pasteItem)
        pasteItem.accelerator = KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN)

        editMenu.items.add(cutItem)
        cutItem.accelerator = KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN)

        editMenu.items.add(deleteTags)
        deleteTags.accelerator = KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN)

        editMenu.items.add(editTags)
        editTags.accelerator = KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN)

        // View
        viewMenu.items.add(minimizeItem)
        minimizeItem.accelerator = KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN)

        viewMenu.items.add(maximizeItem)
        maximizeItem.accelerator = KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)

        viewMenu.items.add(themeItem)
        themeItem.accelerator = KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN)

        viewMenu.items.add(oogaItem)
        oogaItem.accelerator = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)

        menus.add(fileMenu)
        menus.add(editMenu)
        menus.add(viewMenu)
    }

    fun enableNoteOptions() {
        hide(newItem)
        show(undoItem)
        show(redoItem)
        show(copyItem)
        show(cutItem)
        show(pasteItem)
        hide(deleteTags)
        show(saveItem)
        show(exportItem)
    }

    fun enableListOptions() {
        show(newItem)
        show(deleteTags)
        hide(redoItem)
        hide(undoItem)
        hide(copyItem)
        hide(cutItem)
        hide(pasteItem)
        hide(saveItem)
        hide(exportItem)
    }

    fun show(menuItem: MenuItem) {
        menuItem.isVisible = true
        menuItem.isDisable = false
    }

    fun hide(menuItem: MenuItem) {
        menuItem.isVisible = false
        menuItem.isDisable = true
    }
}