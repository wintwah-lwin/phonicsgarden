package com.example.phonicsgarden.data

import com.example.phonicsgarden.data.local.SeedData
import com.example.phonicsgarden.data.local.WordPictures
import com.example.phonicsgarden.data.local.entity.Word
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SeedDataTest {

    @Test
    fun `every bundled word has a picture`() {
        SeedData.words.forEach {
            assertEquals(it.text, WordPictures.emojiFor(it.text), it.emoji)
        }
    }

    @Test
    fun `every word starts with a taught letter and carries that letter's phase`() {
        SeedData.words.forEach { word ->
            val grapheme = SeedData.graphemes.firstOrNull { it.letter == word.firstLetter }
            assertNotNull("No grapheme for ${word.text}", grapheme)
            assertEquals(grapheme!!.phase, word.phase)
            assertTrue(word.text.startsWith(word.firstLetter))
        }
    }

    @Test
    fun `bundled words are marked as bundled`() {
        SeedData.words.forEach { assertEquals(Word.SOURCE_BUNDLED, it.source) }
    }

    @Test
    fun `grapheme ids are unique`() {
        val ids = SeedData.graphemes.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `every taught letter has at least one word to play with`() {
        SeedData.graphemes.forEach { g ->
            assertTrue("No word for ${g.letter}", SeedData.words.any { it.firstLetter == g.letter })
        }
    }
}
