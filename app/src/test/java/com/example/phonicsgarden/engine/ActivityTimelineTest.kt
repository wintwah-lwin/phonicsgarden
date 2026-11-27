package com.example.phonicsgarden.engine

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ActivityTimelineTest {

    private val today = LocalDate.of(2026, 11, 27)

    @Test
    fun `returns seven days ending today, oldest first`() {
        val days = ActivityTimeline.lastDays(emptyList(), today)

        assertEquals(7, days.size)
        assertEquals(LocalDate.of(2026, 11, 21), days.first().date)
        assertEquals(today, days.last().date)
    }

    @Test
    fun `days without practice are filled with zeros`() {
        val days = ActivityTimeline.lastDays(emptyList(), today)
        assertEquals(List(7) { 0 }, days.map { it.answers })
    }

    @Test
    fun `counts are placed on the right day`() {
        val stats = listOf(
            DailyStat("2026-11-25", total = 10, correct = 8),
            DailyStat("2026-11-27", total = 4, correct = 1)
        )
        val days = ActivityTimeline.lastDays(stats, today)

        assertEquals(10, days.first { it.date == LocalDate.of(2026, 11, 25) }.answers)
        assertEquals(8, days.first { it.date == LocalDate.of(2026, 11, 25) }.correct)
        assertEquals(4, days.last().answers)
        assertEquals(1, days.last().correct)
        assertEquals(0, days.first { it.date == LocalDate.of(2026, 11, 26) }.answers)
    }

    @Test
    fun `stats outside the window are ignored`() {
        val stats = listOf(DailyStat("2026-11-01", total = 99, correct = 99))
        val days = ActivityTimeline.lastDays(stats, today)
        assertEquals(0, days.sumOf { it.answers })
    }

    @Test
    fun `window size can be changed`() {
        assertEquals(3, ActivityTimeline.lastDays(emptyList(), today, days = 3).size)
    }

    @Test
    fun `window crosses month boundaries correctly`() {
        val days = ActivityTimeline.lastDays(emptyList(), LocalDate.of(2026, 12, 2))
        assertEquals(LocalDate.of(2026, 11, 26), days.first().date)
    }
}
