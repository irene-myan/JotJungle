// Copyright (c) 2023 Irene Martirosyan, Patrick Telfer, Stephen Cao, Tam Nguyen

package net.codebot.server.persistence

import net.codebot.shared.domain.Tag
import org.jetbrains.exposed.sql.*
import javafx.scene.paint.Color
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class TagsService {
    var initialized = false
    var size = 0

    private object Tags : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 255)
        val color = registerColumn<Color>("color", ColorColumnType)
        override val primaryKey = PrimaryKey(id, name = "PK_Tag_ID")
    }


    fun initializeDB(): List<Tag> {
        if (!initialized) {
            // Connect
            Database.connect("jdbc:sqlite:jotjungle.db")

            // Create notes table
            transaction {
                SchemaUtils.create(Tags)
            }

            initialized = true
        }

        var tags = getTags()
        size = tags.size

        return tags
    }

    fun dropNotesTable() {
        transaction {
            SchemaUtils.drop(Tags)
        }
    }

    fun getTag(tag_id: Int) : Tag {
        val tag = transaction {
            Tags.select() { Tags.id eq tag_id }
                .limit(1)
                .mapNotNull {
                    Tag(
                        it[Tags.name],
                        it[Tags.color],
                        it[Tags.id],
                    )
                }
        }
        if (tag.isEmpty()) {
            return Tag("", id=-1)
        }
        return tag[0]
    }

    fun updateTag(tag: Tag) {
        transaction {
            Tags.update({ Tags.id eq tag.id }) {
                it[name] = tag.name
                it[color] = tag.color
            }
        }
    }

    fun createTag(_name: String, _color: Color) : Tag {
        val id = transaction {
            Tags.insert {
                it[name] = _name
                it[color] = _color
            } get Tags.id
        }
        return Tag(_name, _color, id)
    }

    fun getTags() : List<Tag> {
        val tags = transaction {
            Tags.selectAll().map {
                Tag(
                    it[Tags.name],
                    it[Tags.color],
                    it[Tags.id]
                )
            }
        }
        return tags
    }

    fun deleteTag(id: Int) {
        transaction {
            Tags.deleteWhere { Tags.id eq id }
        }
    }
}