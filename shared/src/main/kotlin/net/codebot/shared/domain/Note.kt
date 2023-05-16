// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.shared.domain

import com.gluonhq.richtextarea.model.Document
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

const val SERVER_ADDRESS = "http://127.0.0.1:8080/"

@Serializable(with = NoteSerializer::class)
class Note(var fileName: String,
           var id: Int,
           var content: Document = Document(""),
           var create_date: LocalDateTime = LocalDateTime.now(),
           var _edit_date: LocalDateTime = LocalDateTime.MIN,
           var tags: MutableList<Tag> = mutableListOf()) {

    var editDate = if (_edit_date > create_date) _edit_date else create_date
    val shortFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    val longFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy hh:mm a")

    override fun toString(): String {
        return fileName
    }

    fun updateEditDate(newEditDate: LocalDateTime = LocalDateTime.now()) {
        editDate = newEditDate
    }

    // Format dates
    fun getLongCreateDate(): String {
        return create_date.format(longFormat)
    }
    fun getLongEditDate(): String {
        return editDate.format(longFormat)
    }
    fun getShortEditDate(): String {
        return editDate.format(shortFormat)
    }

    fun save_note(new_content: Document) {
        content = new_content
        updateEditDate()
        updateNote(this)
    }
    fun assignTags(tagsToSet: List<Tag>) {
        if (tagsToSet.isNotEmpty()) {
            tags.clear()
            tags.addAll(tagsToSet)
            updateNote(this)
        }
    }

    fun removeTags(tagsToRemove: List<Tag>) {
        var newTagsToRemove = mutableListOf<Tag>()
        for (tag in tags) {
            for (tagToRemove in tagsToRemove) {
                if (tag.id == tagToRemove.id) {
                    newTagsToRemove.add(tag)
                }
            }
        }

        tags.removeAll(newTagsToRemove)

        updateNote(this)
    }
}

fun updateNote(note: Note) {
    try {
        // POST
        val string = Json.encodeToString(note)

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_ADDRESS + "notes/"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(string))
            .build()

        client.send(request, HttpResponse.BodyHandlers.ofString())
    } catch (e: Error) {
        println(e.message)
    }
}