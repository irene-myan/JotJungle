// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import net.codebot.application.domain.NoteList
import net.codebot.application.presentation.*
import java.io.File
import java.io.PrintWriter
import java.util.*

class Main : Application() {
    private val settingsFile = "settings.txt"
    private val size = DoubleArray(2)
    private var stageRef: Stage? = null
    private val menuBar = AppMenu()
    var layout = BorderPane()
    private val noteListView = NoteListView(NoteList(), layout, menuBar)
    override fun start(stage: Stage) {
        stageRef = stage

        menuBar.enableListOptions()
        menuBar.quitItem.setOnAction { quit(stage) }
        menuBar.maximizeItem.setOnAction { maximize(stage) }
        menuBar.minimizeItem.setOnAction { minimize(stage) }

        val fileManager = FileManager(noteListView)
        menuBar.exportItem.setOnAction {
            fileManager.showFileChooser(stage)
        }

        menuBar.themeItem.setOnAction {
            var themeDialog = ThemeDialog(::onThemeUpdated)
            themeDialog.show()
        }

        val mainPane = StackPane(layout)

        loadUserSettings()
        stage.scene = Scene(
            mainPane,
            size[0],
            size[1])
        stage.scene.stylesheets.addAll("styles.css", ThemeManager.theme)
        stage.isResizable = true
        stage.title = "Jot Jungle"
        stage.minWidth = 400.0
        stage.minHeight = 400.0
        stage.icons.add(Image("leaves2.png"))
        stage.setOnCloseRequest{ quit(stage) }
        stage.show()

        val ooga = Ooga()
        menuBar.oogaItem.setOnAction {
            ooga.run(mainPane)
        }
    }

    private fun onThemeUpdated(newTheme: String) {
        val cssString = "$newTheme.css"
        ThemeManager.theme = cssString
        stageRef?.scene?.stylesheets?.clear()
        stageRef?.scene?.stylesheets?.addAll("styles.css", ThemeManager.theme)
    }

    private fun minimize(stage: Stage) {
        stage.isIconified = true
    }
    private fun maximize(stage: Stage) {
        stage.isMaximized = !stage.isMaximized
    }
    private fun loadUserSettings() {
        try {
            val sc = Scanner(File(settingsFile))
            var i = 0
            while (i < 2 && sc.hasNextLine()) {
                val line = sc.nextLine()
                size[i] = line.toDouble()
                i++
            }
            val x = sc.nextLine()
            val y = sc.nextLine()
            val theme = sc.nextLine()
            stageRef!!.x = x.toDouble()
            stageRef!!.y = y.toDouble()
            ThemeManager.theme = theme
        } catch (e: Exception) {
            // Default settings
            size[0] = 800.0
            size[1] = 500.0
            ThemeManager.theme = "jungle.css"
        }
    }

    private fun saveUserSettings(stage: Stage) {
        val writer = PrintWriter(settingsFile)
        writer.println(stage.scene.width)
        writer.println(stage.scene.height)
        writer.println(stage.x)
        writer.println(stage.y)
        writer.println(ThemeManager.theme)
        writer.close()
    }
    private fun quit(stage: Stage) {
        saveUserSettings(stage)
        noteListView.thisNoteView?.saveNote()
        stage.close()
    }
}