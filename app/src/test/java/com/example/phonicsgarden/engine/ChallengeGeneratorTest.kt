package com.example.phonicsgarden.engine

import com.example.phonicsgarden.data.local.entity.Grapheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class ChallengeGeneratorTest {

    private val pool = listOf(
        Grapheme(1, "s", "sss", 1),
        Grapheme(2, "a", "ah", 1),
        Grapheme(3, "t", "tuh", 1),
        Grapheme(4, "p", "puh", 1),
        Grapheme(5, "i", "ih", 1),
        Grapheme(6, "n", "nnn", 1)
    )
    private val generator = ChallengeGenerator()

    @Test
    fun `options always contain the correct answer`() {
        repeat(50) { seed ->
            val challenge = generator.generate(pool[0], pool, random = Random(seed))
            assertTrue(challenge.options.contains("s"))
        }
    }

    @Test
    fun `options have requested size and no duplicates`() {
        val challenge = generator.generate(pool[2], pool, optionCount = 4, random = Random(1))
        assertEquals(4, challenge.options.size)
        assertEquals(challenge.options.size, challenge.options.toSet().size)
    }

    @Test
    fun `same seed gives the same challenge`() {
        val a = generator.generate(pool[1], pool, random = Random(42))
        val b = generator.generate(pool[1], pool, random = Random(42))
        assertEquals(a, b)
    }

    @Test
    fun `prompt is the spoken sound and answers are checked correctly`() {
        val challenge = generator.generate(pool[0], pool, random = Random(7))
        assertEquals("sss", challenge.prompt)
        assertTrue(challenge.isCorrect("s"))
        assertFalse(challenge.isCorrect("a"))
    }

    @Test
    fun `small pool gives fewer options instead of crashing`() {
        val small = pool.take(2)
        val challenge = generator.generate(small[0], small, optionCount = 4, random = Random(3))
        assertEquals(2, challenge.options.size)
    }
}
