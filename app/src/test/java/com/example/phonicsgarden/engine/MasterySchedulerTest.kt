package com.example.phonicsgarden.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MasterySchedulerTest {

    private val now = 1_000_000L
    private val day = 24L * 60 * 60 * 1000

    @Test
    fun `correct answer moves up one box`() {
        val result = MasteryScheduler.next(MasteryState(1, 0), correct = true, now = now)
        assertEquals(2, result.box)
    }

    @Test
    fun `box never goes above the maximum`() {
        val result = MasteryScheduler.next(MasteryState(MasteryScheduler.MAX_BOX, 0), true, now)
        assertEquals(MasteryScheduler.MAX_BOX, result.box)
    }

    @Test
    fun `wrong answer drops one box`() {
        val result = MasteryScheduler.next(MasteryState(3, 0), correct = false, now = now)
        assertEquals(2, result.box)
    }

    @Test
    fun `box never goes below zero`() {
        val result = MasteryScheduler.next(MasteryState(0, 0), correct = false, now = now)
        assertEquals(0, result.box)
    }

    @Test
    fun `next review is scheduled further away in higher boxes`() {
        val box1 = MasteryScheduler.next(MasteryState(0, 0), true, now)   // box 1 -> 1 day
        val box3 = MasteryScheduler.next(MasteryState(2, 0), true, now)   // box 3 -> 4 days
        assertEquals(now + day, box1.nextReviewAt)
        assertEquals(now + 4 * day, box3.nextReviewAt)
    }

    @Test
    fun `mastered and due checks`() {
        assertFalse(MasteryScheduler.isMastered(3))
        assertTrue(MasteryScheduler.isMastered(4))
        assertTrue(MasteryScheduler.isDue(MasteryState(1, now - 1), now))
        assertFalse(MasteryScheduler.isDue(MasteryState(1, now + 1), now))
    }
}
