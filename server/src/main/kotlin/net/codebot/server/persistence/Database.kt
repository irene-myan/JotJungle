// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.server.persistence

import com.gluonhq.richtextarea.model.DecorationModel
import com.gluonhq.richtextarea.model.Document
import com.gluonhq.richtextarea.model.TextDecoration
import javafx.scene.paint.Color
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.codebot.shared.domain.Note
import net.codebot.shared.domain.Tag
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NotesService {
    var initialized = false
    var size = 0

    private object Notes : Table() {
        val id = integer("id").autoIncrement()
        val fileName = varchar("fileName", 255)
        val createDate = registerColumn<LocalDateTime>("createDate", LocalDateTimeColumnType)
        val editDate = registerColumn<LocalDateTime>("editDate", LocalDateTimeColumnType)
        val content = registerColumn<Document>("content", DocumentColumnType)
        val tags = varchar("tags", 99999999)
        override val primaryKey = PrimaryKey(id, name = "PK_Note_ID")
    }

    private object Tags : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 255)
        val color = registerColumn<Color>("color", ColorColumnType)
        override val primaryKey = PrimaryKey(id, name = "PK_Tag_ID")
    }


    fun initializeDB(): List<Note> {
        if (!initialized) {
            // Connect
            Database.connect("jdbc:sqlite:jotjungle.db")

            // Create notes table
            transaction {
                SchemaUtils.create(Notes)
                SchemaUtils.create(Tags)
            }

            initialized = true
        }
        var notes = getNotes()
        size = notes.size

        return notes
    }

    fun dropNotesTable() {
        transaction {
            SchemaUtils.drop(Notes)
            SchemaUtils.drop(Tags)
        }
    }

    // Create new note entry in DB
    fun createNote(now: LocalDateTime): Note {
        ++size

        val name = "New Note " + size
        val create_date = LocalDateTime.now()
        val edit_date = LocalDateTime.now()

        // create default empty document
        val arialDecoration = TextDecoration.builder().presets().fontFamily("Arial").build()
        val defaultDecorations = listOf(DecorationModel(0, 0, arialDecoration, null))
        val defaultDocument = Document("", defaultDecorations, 0)

        val id = transaction {
            Notes.insert {
                it[fileName] = name
                it[createDate] = create_date
                it[editDate] = edit_date
                it[content] = defaultDocument
                it[tags] = "[]"
            } get Notes.id

        }
        return Note(name, id = id, create_date = now, _edit_date = now)
    }

    // Returns all entries in Notes table as a List of Notes
    fun getNotes() : List<Note> {
        val notes = transaction {
            Notes.selectAll().map {
                Note(
                    it[Notes.fileName],
                    it[Notes.id],
                    it[Notes.content],
                    it[Notes.createDate],
                    it[Notes.editDate],
                    Json.decodeFromString<MutableList<Tag>>(it[Notes.tags])
                )
            }
        }
        return notes
    }

    fun getSortedNotes(sortParam: String, sortOrder: SortOrder): List<Note> {

        val sortedNotes = transaction {
            var query = Notes.selectAll()
            if (sortParam == "ALPHA") {
                query.orderBy(Notes.fileName to sortOrder)
            }
            if (sortParam == "CREATE") {
                query.orderBy(Notes.createDate to sortOrder)
            }
            if (sortParam == "EDIT") {
                query.orderBy(Notes.editDate to sortOrder)
            }
            query.map {
                Note(
                    it[Notes.fileName],
                    it[Notes.id],
                    it[Notes.content],
                    it[Notes.createDate],
                    it[Notes.editDate],
                    Json.decodeFromString<MutableList<Tag>>(it[Notes.tags])
                )
            }
        }
        return sortedNotes
    }

    fun getNote(note_id: Int) : Note {
        val note = transaction {
            Notes.select() { Notes.id eq note_id }
                .limit(1)
                .mapNotNull {
                    Note(
                        it[Notes.fileName],
                        it[Notes.id],
                        it[Notes.content],
                        it[Notes.createDate],
                        it[Notes.editDate],
                        Json.decodeFromString<MutableList<Tag>>(it[Notes.tags])
                    )
                }
        }
        return note[0]
    }

    // update Note entry in db
    fun updateNote(note: Note) {
        transaction {
            Notes.update({ Notes.id eq note.id }) {
                it[fileName] = note.fileName
                it[createDate] = note.create_date
                it[editDate] = note.editDate
                it[content] = note.content
                it[tags] = Json.encodeToString(note.tags)
            }
        }
    }

    // delete Note entry in db
    fun deleteNote(id: Int) {
        --size
        transaction {
            Notes.deleteWhere { Notes.id eq id }
        }
    }

    fun getTagFilteredNotes(filter: String) : List<Note> {

        if (filter == "null") {
            // no tags were selected, query all notes
            return getNotes()
        } else {
            var tagList = filter.split(",")


            val filteredNotes = transaction {
                var query = Notes.selectAll()
                for (tagID in tagList) {
                    val regex = "\"id\":${tagID}"
                    query = query.orWhere { Notes.tags like "%$regex%" }

                }
                query.map {
                    Note(
                        it[Notes.fileName],
                        it[Notes.id],
                        it[Notes.content],
                        it[Notes.createDate],
                        it[Notes.editDate],
                        Json.decodeFromString<MutableList<Tag>>(it[Notes.tags])
                    )
                }
            }
            return filteredNotes
        }
    }
    fun getSearchNotes(keyword: String) : List<Note> {
        val searchNotes = transaction {
            Notes.select() { Notes.fileName like "%$keyword%" }
                .mapNotNull {
                    Note(
                        it[Notes.fileName],
                        it[Notes.id],
                        it[Notes.content],
                        it[Notes.createDate],
                        it[Notes.editDate],
                        Json.decodeFromString<MutableList<Tag>>(it[Notes.tags])
                    )
                }
        }
        return searchNotes
    }
}
