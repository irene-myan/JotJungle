package net.codebot.shared.domain

import com.gluonhq.richtextarea.model.Document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class NoteTest {
    lateinit var testNote: Note
    lateinit var testTags: List<Tag>
    @BeforeEach
    fun noteInit() {
        testNote = Note("test", 0, Document())
        var zoneId = ZoneId.systemDefault()
        var time = "1681072919"
        testNote.create_date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)
        testNote.editDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.toLong()), zoneId)

        testTags = mutableListOf<Tag>()
    }
/*
    @Test
    // check that note content is updated
    fun save_note() {
        val new_content = Document("save_note test")
        testNote.save_note(new_content)

        assert(testNote.content.text == "save_note test")

    }

    */
    @Test
    // check that no tags assigned
    fun assignNoTags() {
        testNote.assignTags(testTags)
        assert(testNote.tags.isEmpty())
    }
/*
    @Test
    // check that 1 tag is assigned
    fun assignTag() {
        testTags += Tag("test")
        testNote.assignTags(testTags)
        assert(testNote.tags.count() == 1)
        assert(testNote.tags.first().toString() == "test")
    }


    @Test
    // check that no tags removed
    fun removeNoTags() {
        assignTag()
        testNote.removeTags(mutableListOf<Tag>())
        assert(testNote.tags.count() == 1)
        assert(testNote.tags.first().toString() == "test")
    }
    @Test
    // check that the one tag is removed
    fun removeTag() {
        assignTag()
        testNote.removeTags(testTags)
        assert(testNote.tags.isEmpty())
    }

    @Test
    fun assignTwoTags() {
        testTags += Tag("test1")
        testTags += Tag("test2")
        testNote.assignTags(testTags)
        assert(testNote.tags.count() == 2)
        assert(testNote.tags.first().toString() == "test1")
        assert(testNote.tags.last().toString() == "test2")

    }

    @Test
    fun removeTwoTags() {
        assignTwoTags()
        testNote.removeTags(testTags)
        assert(testNote.tags.isEmpty())
    }
    @Test
    fun `remove more tags than available`() {
        testTags += Tag("test1")
        testNote.assignTags(testTags)
        testTags += Tag("test2")
        testNote.removeTags(testTags)
        assert(testNote.tags.isEmpty())
    }

*/


}