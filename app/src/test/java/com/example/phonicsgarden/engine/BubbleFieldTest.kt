package com.example.phonicsgarden.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.random.Random

class BubbleFieldTest {

    private val progressSamples = (0..100).map { it / 100f }

    @Test
    fun `one spec per bubble and none for zero bubbles`() {
        assertEquals(4, BubbleField.layout(4, Random(1)).size)
        assertTrue(BubbleField.layout(0).isEmpty())
    }

    @Test
    fun `bubbles are in different evenly spaced lanes inside the field`() {
        val lanes = BubbleField.layout(4, Random(1)).map { it.lane }.sorted()

        assertEquals(listOf(0.125f, 0.375f, 0.625f, 0.875f), lanes)
    }

    @Test
    fun `bubbles are never level with each other at any moment`() {
        val specs = BubbleField.layout(4, Random(3))
        val minGap = 1f / specs.size - 0.001f

        progressSamples.forEach { progress ->
            val heights = specs.map { BubbleField.verticalFraction(it, progress) }
            for (i in heights.indices) for (j in i + 1 until heights.size) {
                val gap = abs(heights[i] - heights[j])
                val circularGap = minOf(gap, 1f - gap)
                assertTrue("Bubbles $i and $j too close at $progress", circularGap >= minGap)
            }
        }
    }

    @Test
    fun `vertical position always stays between zero and one and wraps round`() {
        val spec = BubbleSpec(lane = 0.5f, phase = 0.9f, swayPhase = 0f)

        progressSamples.forEach {
            assertTrue(BubbleField.verticalFraction(spec, it) in 0f..1f)
        }
        // Starts near the top, wraps to the bottom, then keeps rising.
        assertEquals(0.9f, BubbleField.verticalFraction(spec, 0f), 0.0001f)
        assertEquals(0.05f, BubbleField.verticalFraction(spec, 0.15f), 0.0001f)
    }

    @Test
    fun `bubbles only rise as time moves forward, apart from the wrap`() {
        val spec = BubbleSpec(lane = 0.5f, phase = 0.2f, swayPhase = 0f)
        assertTrue(BubbleField.verticalFraction(spec, 0.3f) > BubbleField.verticalFraction(spec, 0.2f))
    }

    @Test
    fun `sway stays close to the lane`() {
        val spec = BubbleSpec(lane = 0.5f, phase = 0f, swayPhase = 0.37f)

        progressSamples.forEach {
            val offset = abs(BubbleField.horizontalCenter(spec, it) - spec.lane)
            assertTrue(offset <= BubbleField.SWAY_AMPLITUDE + 0.0001f)
        }
    }

    @Test
    fun `the loop repeats without a jump`() {
        BubbleField.layout(4, Random(5)).forEach { spec ->
            assertEquals(
                BubbleField.horizontalCenter(spec, 0f),
                BubbleField.horizontalCenter(spec, 1f),
                0.0001f
            )
        }
    }

    @Test
    fun `same seed gives the same layout`() {
        assertEquals(BubbleField.layout(4, Random(9)), BubbleField.layout(4, Random(9)))
    }

    @Test
    fun `different seeds usually shuffle the lanes`() {
        val layouts = (1..10).map { seed -> BubbleField.layout(4, Random(seed)).map { it.lane } }.toSet()
        assertTrue(layouts.size > 1)
    }
}
