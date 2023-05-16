// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.presentation

import javafx.event.Event
import javafx.event.EventType
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.util.Callback
import net.codebot.application.presentation.tag.TagPill
import net.codebot.shared.domain.Note

class ListCellEvent(eventType: EventType<out Event?>?)
    : Event(eventType) {
    companion object {
        val EMPTY = EventType(ANY, "EMPTY")
    }
}

class NoteListItemFactory : Callback<ListView<Note>, ListCell<Note>> {
    override fun call(param: ListView<Note>?): ListCell<Note> {
        return NoteListItem()
    }
}

private class NoteListItem : ListCell<Note>() {
    val noteTitle = Label()
    val date = Label()

    val container = HBox()
    val rightContainer = HBox()
    val leftContainer = HBox()
    val tagContainer = HBox()

    init {
        container.children.addAll(leftContainer, rightContainer)
        container.alignment = Pos.CENTER_LEFT

        leftContainer.children.addAll(noteTitle, tagContainer)
        leftContainer.alignment = Pos.CENTER_LEFT
        leftContainer.spacing = 20.0

        HBox.setHgrow(rightContainer, Priority.ALWAYS)
        rightContainer.alignment = Pos.CENTER_RIGHT
        rightContainer.children.addAll(date)

        tagContainer.alignment = Pos.CENTER_LEFT
        tagContainer.spacing = 10.0

        // Clear select when clicking on an empty cell
        setOnMouseClicked { _ ->
            if (isEmpty) {
                fireEvent(ListCellEvent(ListCellEvent.EMPTY))
            }
        }

    }

    // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Cell.html#updateItem-T-boolean-
    override fun updateItem(item: Note?, empty: Boolean) {
        super.updateItem(item, empty)

        if (empty || item == null) {
            text = null
            graphic = null
        } else {
//            val updatedItem = item.updatedNote()
            noteTitle.text = item.toString()
            date.text = "Last Edited: " + item.getShortEditDate()

            tagContainer.children.clear()

            for (tag in item.tags) {
                val tagPill = TagPill(tag)
                tagContainer.children.add(tagPill)
            }
            graphic = container
        }
    }
}
