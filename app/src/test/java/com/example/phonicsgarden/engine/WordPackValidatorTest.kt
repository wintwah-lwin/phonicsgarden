package com.example.phonicsgarden.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WordPackValidatorTest {

    private val taught = mapOf("s" to 1, "t" to 1, "m" to 2, "b" to 3)

    private fun validate(vararg entries: Pair<String, String>) =
        WordPackValidator.validate(entries.map { PackEntry(it.first, it.second) }, taught)

    @Test
    fun `accepts a simple word with a picture and gives it the phase of its letter`() {
        val result = validate("sun" to "☀️", "moon" to "🌙", "bus" to "🚌")

        assertEquals(3, result.accepted.size)
        assertEquals(0, result.rejected)
        assertEquals(listOf(1, 2, 3), result.accepted.map { it.phase })
        assertEquals("m", result.accepted[1].firstLetter)
    }

    @Test
    fun `tidies case and spaces`() {
        val result = validate("  Sun " to " ☀️ ")
        assertEquals("sun", result.accepted.single().text)
        assertEquals("☀️", result.accepted.single().emoji)
    }

    @Test
    fun `rejects words that are not plain lowercase letters`() {
        val result = validate(
            "sun2" to "☀️", "ice-cream" to "🍦", "ice cream" to "🍦", "sun!" to "☀️", "s" to "☀️"
        )
        assertTrue(result.accepted.isEmpty())
        assertEquals(5, result.rejected)
    }

    @Test
    fun `rejects words that are too long`() {
        val result = validate("s".repeat(WordPackValidator.MAX_WORD_LENGTH + 1) to "☀️")
        assertTrue(result.accepted.isEmpty())
    }

    @Test
    fun `rejects words that do not start with a taught letter`() {
        val result = validate("zebra" to "🦓")
        assertTrue(result.accepted.isEmpty())
        assertEquals(1, result.rejected)
    }

    @Test
    fun `rejects pictures that could carry text`() {
        val result = validate(
            "sun" to "",
            "sock" to "   ",
            "star" to "a",
            "swan" to "1",
            "seal" to "🦭 🦭",
            "snake" to "🐍".repeat(WordPackValidator.MAX_EMOJI_LENGTH)
        )
        assertTrue(result.accepted.isEmpty())
        assertEquals(6, result.rejected)
    }

    @Test
    fun `ignores duplicates`() {
        val result = validate("sun" to "☀️", "SUN" to "🌞", "sun" to "☀️")
        assertEquals(1, result.accepted.size)
        assertEquals(2, result.rejected)
        assertEquals("☀️", result.accepted.single().emoji)
    }

    @Test
    fun `caps the number of accepted words`() {
        val letters = ('a'..'z').toList()
        val entries = (0 until 300).map { i ->
            PackEntry("s${letters[i / 26 % 26]}${letters[i % 26]}", "⭐")
        }
        val result = WordPackValidator.validate(entries, taught)

        assertEquals(WordPackValidator.MAX_WORDS, result.accepted.size)
        assertEquals(100, result.rejected)
    }

    @Test
    fun `an empty pack is valid and adds nothing`() {
        val result = WordPackValidator.validate(emptyList(), taught)
        assertTrue(result.accepted.isEmpty())
        assertEquals(0, result.rejected)
    }
}
