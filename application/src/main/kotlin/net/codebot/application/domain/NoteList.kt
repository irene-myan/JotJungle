// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.application.domain

import javafx.scene.paint.Color
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.codebot.application.presentation.IView
import net.codebot.shared.domain.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.LocalDateTime

class NoteList {
    private var list = mutableListOf<Note>()
    val observers = mutableListOf<IView>()
    private var tags = mutableListOf<Tag>()

    init {
        // connect(), createNotesTable()
        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "initialize_notes"))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            list = Json.decodeFromString<MutableList<Note>>(response.body())

        } catch (e: Error) {
            println(e.message)
        }

        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "initialize_tags"))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            tags = Json.decodeFromString<MutableList<Tag>>(response.body())

        } catch (e: Error) {
            println(e.message)
        }
    }

    fun getSize() : Int = list.size

    fun getNotes() : List<Note> {
        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "notes"))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            list = Json.decodeFromString<MutableList<Note>>(response.body())
            return list
        } catch (e: Error) {
            println(e.message)
        }
        // Shouldn't reach this line
        return mutableListOf()
    }

    fun getSortedNotes(sortParam : String , sortOrder : String) : List<Note> {
        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "notes/sort/${sortParam}/${sortOrder}"))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            list = Json.decodeFromString<MutableList<Note>>(response.body())
            return list
        } catch (e: Error) {
            println(e.message)
        }
        // Shouldn't reach this line
        return mutableListOf()
    }

    fun getFilteredNotes(tagIDs : String) : List<Note> {
        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "notes/filter/${tagIDs}"))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            list = Json.decodeFromString<MutableList<Note>>(response.body())
            return list
        } catch (e: Error) {
            println(e.message)
        }
        // Shouldn't reach this line
        return mutableListOf()
    }
    fun searchNotes(keyword : String) : List<Note> {
        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "notes/search/${keyword}"))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            list = Json.decodeFromString<MutableList<Note>>(response.body())
            return list
        } catch (e: Error) {
            println(e.message)
        }
        // Shouldn't reach this line
        return mutableListOf()
    }

    fun getTags() : List<Tag> {
        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "tags"))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            tags = Json.decodeFromString<MutableList<Tag>>(response.body())
            return tags.toList()
        } catch (e: Error) {
            println(e.message)
        }
        // Shouldn't reach this line
        return listOf()
    }

    fun getNote(idx: Int) : Note {
        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "notes/" + list[idx].id))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            val note = Json.decodeFromString<Note>(response.body())

            list[idx] = note
            return note
        } catch (e: Error) {
            print(e.message)
        }
        // Shouldn't reach this line
        return Note("",-1)
    }

    fun addTag(tag: Tag) : Tag {
        try {
            // POST
            val string = Json.encodeToString(TagSerialzer, tag)
            val client = HttpClient.newBuilder().build();
            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "new_tag"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(string))
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var new_tag = Json.decodeFromString<Tag>(response.body())
            tags.add(new_tag)
            return new_tag
        } catch (e: Error) {
            println(e.message)
        }
        return Tag("")
    }

    fun deleteTags(tagsToRemove: List<Tag>) {
        for (tag in tagsToRemove) {
            try {
                // GET
                val client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .connectTimeout(Duration.ofSeconds(20))
                    .build()

                val request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_ADDRESS + "tags/" + tag.id + "/delete"))
                    .GET()
                    .build()

                client.send(request, HttpResponse.BodyHandlers.ofString())
            } catch (e: Error) {
                print(e.message)
            }
        }
        for (note in getNotes()) {
            note.removeTags(tagsToRemove)
        }
    }

    fun editTag(tag: Tag, name: String, col: Color) {
        tag.name = name
        tag.color = col
        try {
            // POST
            val string = Json.encodeToString(tag)

            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "tags/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(string))
                .build()

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (e: Error) {
            println(e.message)
        }
    }

    fun register(view: IView) {
        observers.add(view)
    }

    fun create_new_note() {
        // new note created in db
        try {
            // POST
            val string = Json.encodeToString(LocalDateTimeSerializer, LocalDateTime.now())
            val client = HttpClient.newBuilder().build();
            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "new_note"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(string))
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            var new_note = Json.decodeFromString<Note>(response.body())
            list.add(new_note)
            notifyAdd(new_note)
        } catch (e: Error) {
            println(e.message)
        }
    }

    fun delete_note(idx: Int) {
        // Delete from DB
        val note_id = list[idx].id

        try {
            // GET
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(20))
                .build()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_ADDRESS + "notes/" + note_id + "/delete"))
                .GET()
                .build()

            client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (e: Error) {
            println(e.message)
        }

        list.removeAt(idx)
    }

    fun notifyAdd(note: Note) {
        for (observer in observers) {
            observer.addedNote(note)
        }
    }
}

