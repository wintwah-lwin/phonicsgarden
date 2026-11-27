package com.example.phonicsgarden.engine

import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Word
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class WordChallengeGeneratorTest {

    private val s = Grapheme(1, "s", "sss", 1)
    private val t = Grapheme(2, "t", "tuh", 1)
    private val m = Grapheme(3, "m", "mmm", 1)
    private val c = Grapheme(4, "c", "kuh", 2)
    private val k = Grapheme(5, "k", "kuh", 2)
    private val graphemes = listOf(s, t, m, c, k)

    private val words = listOf(
        Word(text = "sun", firstLetter = "s", emoji = "🟢"), Word(text = "sock", firstLetter = "s", emoji = "🟢"),
        Word(text = "tent", firstLetter = "t", emoji = "🟢"), Word(text = "tree", firstLetter = "t", emoji = "🟢"),
        Word(text = "moon", firstLetter = "m", emoji = "🟢"),
        Word(text = "cat", firstLetter = "c", emoji = "🟢"), Word(text = "kite", firstLetter = "k", emoji = "🟢")
    )
    private val generator = WordChallengeGenerator()

    @Test
    fun `options contain exactly one word that starts with the target sound`() {
        repeat(50) { seed ->
            val challenge = generator.generate(s, graphemes, words, random = Random(seed))!!
            assertEquals(1, challenge.options.count { challenge.isCorrect(it) })
        }
    }

    @Test
    fun `options are the requested size with no duplicates`() {
        val challenge = generator.generate(t, graphemes, words, optionCount = 3, random = Random(1))!!
        assertEquals(3, challenge.options.size)
        assertEquals(3, challenge.options.toSet().size)
    }

    @Test
    fun `letters that share a sound are both accepted and never used as distractors`() {
        val challenge = generator.generate(c, graphemes, words, random = Random(5))!!
        assertTrue(challenge.isCorrect("cat"))
        assertTrue(challenge.isCorrect("kite"))
        // Only one of cat/kite can be offered, so the child is never shown two right answers.
        assertEquals(1, challenge.options.count { it == "cat" || it == "kite" })
    }

    @Test
    fun `every option comes with its picture`() {
        val challenge = generator.generate(s, graphemes, words, random = Random(4))!!
        assertEquals(challenge.options.toSet(), challenge.pictures.keys)
    }

    @Test
    fun `returns null when the sound has no words`() {
        val result = generator.generate(m, graphemes, words.filter { it.firstLetter != "m" })
        assertNull(result)
    }

    @Test
    fun `returns null when there is nothing to compare against`() {
        val onlyS = words.filter { it.firstLetter == "s" }
        assertNull(generator.generate(s, graphemes, onlyS))
    }

    @Test
    fun `same seed gives the same challenge`() {
        val a = generator.generate(s, graphemes, words, random = Random(9))
        val b = generator.generate(s, graphemes, words, random = Random(9))
        assertNotNull(a)
        assertEquals(a, b)
    }

    @Test
    fun `prompt mentions the spoken sound`() {
        val challenge = generator.generate(s, graphemes, words, random = Random(2))!!
        assertTrue(challenge.prompt.endsWith("sss"))
    }
}
