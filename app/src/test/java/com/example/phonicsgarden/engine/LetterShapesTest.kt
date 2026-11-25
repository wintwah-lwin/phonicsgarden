package com.example.phonicsgarden.engine

import com.example.phonicsgarden.data.local.SeedData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.hypot

class LetterShapesTest {

    private val letters = SeedData.graphemes.map { it.letter }

    /** Simulates a smooth finger path by adding points between consecutive waypoints. */
    private fun densify(points: List<TracePoint>, step: Float = 0.03f): List<TracePoint> {
        val result = mutableListOf<TracePoint>()
        points.zipWithNext().forEach { (a, b) ->
            val steps = (hypot(b.x - a.x, b.y - a.y) / step).toInt().coerceAtLeast(1)
            for (i in 0 until steps) {
                val t = i.toFloat() / steps
                result += TracePoint(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t)
            }
        }
        result += points.last()
        return result
    }

    @Test
    fun `every taught letter has a tracing guide`() {
        letters.forEach { assertTrue("Missing shape for $it", LetterShapes.hasShape(it)) }
    }

    @Test
    fun `all waypoints sit inside the unit square`() {
        letters.forEach { letter ->
            LetterShapes.waypoints(letter).forEach { p ->
                assertTrue("$letter x out of range: ${p.x}", p.x in 0f..1f)
                assertTrue("$letter y out of range: ${p.y}", p.y in 0f..1f)
            }
        }
    }

    @Test
    fun `every stroke has at least one point`() {
        letters.forEach { letter ->
            LetterShapes.strokes(letter)!!.forEach { assertTrue(it.isNotEmpty()) }
        }
    }

    @Test
    fun `tracing along the guide in order is accepted for every letter`() {
        letters.forEach { letter ->
            val path = densify(LetterShapes.waypoints(letter))
            assertTrue("Tracing $letter should pass", TraceValidator.isValid(path, LetterShapes.waypoints(letter)))
        }
    }

    @Test
    fun `scribbling far from the letter is rejected for every letter`() {
        val scribble = listOf(TracePoint(0.97f, 0.97f), TracePoint(0.95f, 0.99f), TracePoint(0.99f, 0.95f))
        letters.forEach { letter ->
            assertFalse("Scribble should fail for $letter", TraceValidator.isValid(scribble, LetterShapes.waypoints(letter)))
        }
    }

    @Test
    fun `t requires the stem before the crossbar`() {
        val strokes = LetterShapes.strokes("t")!!
        val crossbarFirst = densify(strokes[1]) + densify(strokes[0])
        assertFalse(TraceValidator.isValid(crossbarFirst, LetterShapes.waypoints("t")))
    }

    @Test
    fun `i dot is a single point and the stem comes first`() {
        val strokes = LetterShapes.strokes("i")!!
        assertEquals(1, strokes[1].size)
        val dotThenStem = strokes[1] + densify(strokes[0])
        assertFalse(TraceValidator.isValid(dotThenStem, LetterShapes.waypoints("i")))
    }

    @Test
    fun `multi stroke letters report the right number of strokes`() {
        assertEquals(1, LetterShapes.strokes("o")!!.size)
        assertEquals(2, LetterShapes.strokes("t")!!.size)
        assertEquals(3, LetterShapes.strokes("k")!!.size)
        assertNotNull(LetterShapes.strokes("m"))
    }

    @Test
    fun `unknown letters return no shape`() {
        assertFalse(LetterShapes.hasShape("z"))
        assertTrue(LetterShapes.waypoints("z").isEmpty())
    }
}
