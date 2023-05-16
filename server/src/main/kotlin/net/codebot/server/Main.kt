// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.server

import kotlinx.serialization.json.Json
import net.codebot.server.persistence.NotesService
import net.codebot.server.persistence.TagsService
import net.codebot.shared.domain.LocalDateTimeSerializer
import net.codebot.shared.domain.Note
import net.codebot.shared.domain.Tag
import org.jetbrains.exposed.sql.SortOrder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@SpringBootApplication
class ServerApplication


fun main(args: Array<String>) {
    try {
        runApplication<ServerApplication>(*args)
    } catch (e: Error) {
        println(e.message)
    }
}

@RestController
@RequestMapping("/new_note")
class MessageResource(val service: NotesService) {
    @PostMapping
    fun index(@RequestBody now: String): Note {
        return service.createNote(Json.decodeFromString<LocalDateTime>( LocalDateTimeSerializer, now))
    }
}

@RestController
@RequestMapping("/notes")
class NotesResource(val service: NotesService) {
    @GetMapping
    fun index(): List<Note> {
        return service.getNotes()
    }

    @GetMapping("/sort/{sortParam}/{sortOrder}")
    fun sort(@PathVariable sortParam: String, @PathVariable sortOrder: SortOrder) : List<Note> {
        return service.getSortedNotes(sortParam, sortOrder)
    }

    @GetMapping("/filter/{tagIDs}")
    fun tagFilter(@PathVariable("tagIDs") tagIDs: String) : List<Note> {
        return service.getTagFilteredNotes(tagIDs)
    }

    @GetMapping("/search/{keyword}")
    fun search(@PathVariable("keyword") keyword: String) : List<Note> {
        return service.getSearchNotes(keyword)
    }

    @GetMapping("/{note_id}/delete")
    fun delete(@PathVariable note_id: Int) : String {
        service.deleteNote(note_id)
        return "Deleted"
    }
    @GetMapping("/{note_id}")
    fun get(@PathVariable note_id: Int) : Note {
        return service.getNote(note_id)
    }

    @PostMapping
    fun post(@RequestBody note: Note) {
        service.updateNote(note)
    }
}

@RestController
@RequestMapping("/initialize_notes")
class InitializeNotes(val service: NotesService) {
    @GetMapping
    fun index(): List<Note> {
        return service.initializeDB()
    }
}

@RestController
@RequestMapping("/initialize_tags")
class InitializeTags(val service: TagsService) {
    @GetMapping
    fun index(): List<Tag> {
        return service.initializeDB()
    }
}

@RestController
@RequestMapping("/tags")
class Tags(val service: TagsService) {
    @GetMapping
    fun index(): List<Tag> {
        return service.getTags()
    }
    @GetMapping("/{id}/delete")
    fun delete(@PathVariable id: Int) : String {
        service.deleteTag(id)
        return "Deleted"
    }
    @GetMapping("/{id}")
    fun get(@PathVariable id: Int) : Tag {
        return service.getTag(id)
    }

    @PostMapping
    fun post(@RequestBody tag: Tag) {
        service.updateTag(tag)
    }
}

@RestController
@RequestMapping("/new_tag")
class NewTagService(val service: TagsService) {
    @PostMapping
    @ResponseBody
    fun index(@RequestBody tag: Tag): Tag {
        return service.createTag(tag.name, tag.color)
    }
}