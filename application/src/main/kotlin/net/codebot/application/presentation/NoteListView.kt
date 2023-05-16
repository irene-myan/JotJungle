// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.geometry.Side
import javafx.scene.control.*
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import net.codebot.application.domain.NoteList
import net.codebot.application.presentation.tag.EditTagDialog
import net.codebot.application.presentation.tag.TagCheckboxDialog
import net.codebot.shared.domain.Note
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.lineawesome.LineAwesomeSolid

class NoteListView(private val note_list: NoteList, private val layout: BorderPane,
                   private val menuBar: AppMenu) : VBox(), IView {
    private val note_names = ListView<Note>()
    private val noteToolBar = ToolBar()

    private val newNoteButton = Button()

    private val searchPane = StackPane()
    private val leftSearchButton = Button()
    private val clearSearchButton = Button()

    private val sortButton = MenuButton()

    private val contextMenu = ContextMenu()
    private val openItem = MenuItem("Open")
    private val renameItem = MenuItem("Rename")
    private val deleteItem = MenuItem("Delete")
    private val propertiesItem = MenuItem("Properties")
    private val tagFilterButton = Button("Filter by Tag")

    var thisNoteView: NoteView? = null

    init {
        setVgrow(note_names, Priority.ALWAYS)

        // Setup layout + menu bar
        setUpWindow()

        note_list.register(this)

        setUpNoteNames()
        setTooltips()
    }

    private fun setUpNoteNames() {
        note_names.items.clear()
        for (note in note_list.getNotes()) {
            note_names.items.add(note)
        }
        note_names.setOnKeyReleased { e ->
            val code = e.code
            if (getSelectedIndex() >=0) {
                when (code) {
                    KeyCode.ENTER -> openItem.fire()
                    KeyCode.DELETE -> deleteItem.fire()
                    KeyCode.R -> renameItem.fire()
                    KeyCode.P -> propertiesItem.fire()
                    else -> {}
                }
            }
        }
    }

    private fun delete_note() {
        note_list.delete_note(getSelectedIndex())

        note_names.items.removeAt(getSelectedIndex())
        note_names.selectionModel.clearSelection()
    }

    private fun setUpWindow() {
        // Setup window layout
        layout.top = menuBar
        layout.center = this
        layout.addEventHandler(LayoutEvent.LIST) {
            setUpNoteNames()
            layout.center = this
            menuBar.enableListOptions()
            thisNoteView = null
        }

        layout.addEventHandler(LayoutEvent.NOTE) {
            layout.center = this.createNoteView()
            setUpNoteNames()
            menuBar.enableNoteOptions()
        }

        newNoteButton.setOnAction {
            note_list.create_new_note()
            note_names.selectionModel.select(note_list.getSize())
            fireEvent(LayoutEvent(LayoutEvent.NOTE))
        }
        val plusIcon = FontIcon(LineAwesomeSolid.PLUS)
        plusIcon.iconSize = 20
        newNoteButton.graphic = plusIcon

        setUpMenu()
        setupTagFilter()
        setUpSearchBar()
        setUpSort()
        setUpContextMenu()

        val spacer = Pane()
        HBox.setHgrow(spacer, Priority.SOMETIMES)

        noteToolBar.items.addAll(newNoteButton, spacer,
            tagFilterButton,
            Separator(Orientation.VERTICAL),
            searchPane,
            sortButton)
        noteToolBar.padding = Insets(10.0)

        // Note list setup
        note_names.styleClass.add("note-list-view")
        note_names.fixedCellSize = 30.0
        note_names.setOnMouseClicked { mouseEvent ->
            if (mouseEvent.clickCount == 2 && mouseEvent.button == MouseButton.PRIMARY
                && note_names.selectionModel.selectedIndex >= 0) {
                fireEvent(LayoutEvent(LayoutEvent.NOTE))
            }
        }

        note_names.cellFactory = NoteListItemFactory()

        this.children.addAll(noteToolBar, note_names)
    }

    private fun setupTagFilter() {
        tagFilterButton.setOnAction {
            var tagCheckBoxDialog = TagCheckboxDialog(note_list, null, false)
            tagCheckBoxDialog.title = "Select Tags to Filter by"
            val filterButton = ButtonType("Filter", ButtonData.OK_DONE)
            tagCheckBoxDialog.dialogPane.buttonTypes.add(filterButton)

            var res = tagCheckBoxDialog.showAndWait()
            res.ifPresent {
                val tagsSelected = it;
                var tagIDs = ""
                if (tagsSelected.isNotEmpty()) {
                    for ((i, tag) in tagsSelected.withIndex()) {
                        tagIDs += if (i != tagsSelected.size - 1) {
                            "${tag.id},"
                        } else {
                            "${tag.id}"
                        }
                    }
                } else {
                    tagIDs = "null"
                }

                var newNotes: List<Note> = note_list.getFilteredNotes(tagIDs)
                note_names.items.clear()
                note_names.items.addAll(newNotes)
            }
        }
    }

    private fun setUpMenu() {
        menuBar.newItem.setOnAction {
            note_list.create_new_note()
            layout.fireEvent(LayoutEvent(LayoutEvent.NOTE))
        }

        menuBar.deleteTags.setOnAction {
            val deleteTagDialog = TagCheckboxDialog(note_list)
            deleteTagDialog.title = "Your Tags"
            val deleteButton = ButtonType("Delete", ButtonData.OK_DONE)
            deleteTagDialog.dialogPane.buttonTypes.addAll(deleteButton)

            val res = deleteTagDialog.showAndWait()
            res.ifPresent {
                if (it.isNotEmpty()) {
                    note_list.deleteTags(it)
                    setUpNoteNames()
                }
            }
        }

        menuBar.editTags.setOnAction {
            val editTagDialog = EditTagDialog(note_list)
            val res = editTagDialog.showAndWait()
            res.ifPresent {
                setUpNoteNames()
            }
        }
    }

    private fun setUpSearchBar() {
        val searchField = TextField()

        StackPane.setMargin(searchField, Insets(0.0, 15.0, 0.0, 15.0))
        searchField.padding = Insets(5.0, 27.0, 5.0, 27.0)
        searchField.id = "search-field"

        StackPane.setAlignment(leftSearchButton, Pos.CENTER_LEFT)
        leftSearchButton.id = "search-button"
        leftSearchButton.styleClass.add("search-icon")
        StackPane.setMargin(leftSearchButton, Insets(0.0, 0.0, 0.0, 25.0))

        StackPane.setAlignment(clearSearchButton, Pos.CENTER_RIGHT)
        clearSearchButton.id = "delete-button"
        clearSearchButton.styleClass.add("search-icon")
        StackPane.setMargin(clearSearchButton, Insets(0.0, 25.0, 0.0, 0.0))

        searchPane.children.addAll(searchField, clearSearchButton, leftSearchButton)
        searchField.promptText = "Search"

        leftSearchButton.setOnAction { _ ->

            if (searchField.text.isEmpty()) {
                var newNotes: List<Note> = note_list.getNotes()
                note_names.items.clear()
                note_names.items.addAll(newNotes)
            } else {
                var newNotes: List<Note> = note_list.searchNotes(searchField.text)
                note_names.items.clear()
                note_names.items.addAll(newNotes)
            }
        }

        clearSearchButton.setOnAction { _ ->
            searchField.clear()
            var newNotes: List<Note> = note_list.getNotes()
            note_names.items.clear()
            note_names.items.addAll(newNotes)
        }

        searchField.setOnKeyPressed{ ke ->
            run {
                when (ke.code) {
                    KeyCode.ENTER -> {
                        if (searchField.text.isEmpty()) {
                            var newNotes: List<Note> = note_list.getNotes()
                            note_names.items.clear()
                            note_names.items.addAll(newNotes)
                        } else {
                            var newNotes: List<Note> = note_list.searchNotes(searchField.text)
                            note_names.items.clear()
                            note_names.items.addAll(newNotes)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setUpSort() {
        val sortImage = ImageView("sort-down.png")
        sortImage.fitWidth = 17.0
        sortImage.isPreserveRatio = true
        sortImage.isSmooth = true
        sortImage.isCache = true
        sortButton.graphic = sortImage

        val sortGroup = ToggleGroup()
        val sortUp = FontIcon(LineAwesomeSolid.ARROW_UP)
        val sortDown = FontIcon(LineAwesomeSolid.ARROW_DOWN)

        val sortAlpha = RadioMenuItem("Alphabetical")
        val sortCreate = RadioMenuItem("Create date")
        val sortEdit = RadioMenuItem("Edit date")
        sortAlpha.toggleGroup = sortGroup
        sortCreate.toggleGroup = sortGroup
        sortEdit.toggleGroup = sortGroup

        var lastSort = RadioMenuItem()

        sortAlpha.setOnAction { _ ->
            if (lastSort != sortAlpha) {
                lastSort.graphic = null
                lastSort = sortAlpha
            }

            var newNotes: List<Note> = if(lastSort.graphic == sortUp) {
                note_list.getSortedNotes("ALPHA", "DESC")
            } else {
                note_list.getSortedNotes("ALPHA", "ASC")
            }

            note_names.items.clear()
            note_names.items.addAll(newNotes)
            flipSortIcon(lastSort, sortUp, sortDown)

        }
        sortCreate.setOnAction { _ ->
            if (lastSort != sortCreate) {
                lastSort.graphic = null
                lastSort = sortCreate
            }

            var newNotes: List<Note> = if(lastSort.graphic == sortUp) {
                note_list.getSortedNotes("CREATE", "DESC")
            } else {
                note_list.getSortedNotes("CREATE", "ASC")
            }

            note_names.items.clear()
            note_names.items.addAll(newNotes)
            flipSortIcon(lastSort, sortUp, sortDown)
        }
        sortEdit.setOnAction { _ ->
            if (lastSort != sortEdit) {
                lastSort.graphic = null
                lastSort = sortEdit
            }
            var newNotes: List<Note> = if(lastSort.graphic == sortUp) {
                note_list.getSortedNotes("EDIT", "DESC")
            } else {
                note_list.getSortedNotes("EDIT", "ASC")
            }

            note_names.items.clear()
            note_names.items.addAll(newNotes)
            flipSortIcon(lastSort, sortUp, sortDown)
        }

        sortButton.items.addAll( sortAlpha, sortCreate, sortEdit)
        sortButton.popupSide = Side.BOTTOM
        lastSort = sortCreate
        sortCreate.isSelected = true
    }

    private fun setUpContextMenu() {
        openItem.setOnAction {_ ->
            fireEvent(LayoutEvent(LayoutEvent.NOTE))
        }

        renameItem.setOnAction {_ ->
            val note_view = createNoteView()
            layout.center = note_view
            setUpNoteNames()
            menuBar.enableNoteOptions()
            note_view.focusTitle()
        }

        val deleteAlert = Alert(Alert.AlertType.CONFIRMATION)
        deleteAlert.headerText = "Delete note"
        deleteItem.setOnAction {_ ->
            deleteAlert.contentText = "Do you want to delete \"" + note_names.items[getSelectedIndex()].fileName+ "\"?"
            deleteAlert.setOnCloseRequest { _ ->
                if(deleteAlert.result.text == "OK") {
                    delete_note()
                }
            }
            deleteAlert.show()
        }


        val propertiesAlert = Alert(Alert.AlertType.INFORMATION)
        propertiesItem.setOnAction {_ ->
            val curNote = note_names.items[getSelectedIndex()]
            propertiesAlert.headerText = curNote.fileName
            propertiesAlert.contentText =
                "Created: ${curNote.getLongCreateDate()}\nLast Edited: ${curNote.getLongEditDate()}"
            propertiesAlert.show()
        }

        val tagItem = MenuItem("Manage Tags")
        tagItem.setOnAction {
            val curNote = note_names.items[getSelectedIndex()]

            val manageTagDialog = TagCheckboxDialog(note_list, curNote)
            manageTagDialog.title = "Manage This Note's Tags"
            val applyButton = ButtonType("Apply", ButtonData.OK_DONE)
            manageTagDialog.dialogPane.buttonTypes.add(applyButton)

            val res = manageTagDialog.showAndWait()
            res.ifPresent {
                curNote.assignTags(it)
                setUpNoteNames()
            }
        }

        contextMenu.items.addAll(openItem, renameItem, deleteItem, propertiesItem, tagItem)
        note_names.contextMenu = contextMenu

        note_names.addEventFilter(ListCellEvent.EMPTY, EventHandler {
            note_names.selectionModel.clearSelection()
            Platform.runLater { note_names.contextMenu.hide() }
        })
    }

    private fun getSelectedIndex() : Int {
        return note_names.selectionModel.selectedIndex
    }

    private fun createNoteView() : NoteView {
        if (getSelectedIndex() < 0) {
            throw Exception("Error: no note selected")
        }
        var nv = NoteView(note_list.getNote(getSelectedIndex()))
        thisNoteView = nv
        menuBar.copyItem.setOnAction { nv.editor.copy(it) }
        menuBar.cutItem.setOnAction { nv.editor.cut(it) }
        menuBar.pasteItem.setOnAction { nv.editor.paste(it) }
        menuBar.undoItem.setOnAction { nv.editor.undo(it) }
        menuBar.redoItem.setOnAction { nv.editor.redo(it) }
        menuBar.saveItem.setOnAction { nv.saveNote() }

        return nv
    }

    override fun addedNote(note: Note) {
        // Add notes name to list of notes
        note_names.items.add(note)

        // Select new note
        note_names.selectionModel.selectLast()
    }

    // Not called on SortNone
    private fun flipSortIcon(item: RadioMenuItem, upIcon: FontIcon, downIcon: FontIcon) {
        if (item.graphic == null || item.graphic == downIcon) {
            item.graphic = upIcon
            return
        }
        item.graphic = downIcon
    }

    private fun setTooltips() {
        newNoteButton.tooltip = Tooltip("Create new note")

        leftSearchButton.tooltip = Tooltip("Search")
        clearSearchButton.tooltip = Tooltip("Clear")

        sortButton.tooltip = Tooltip("Sort")
    }
}