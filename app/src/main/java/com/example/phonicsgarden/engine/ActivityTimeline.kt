package com.example.phonicsgarden.engine

import java.time.LocalDate

/** One row of the per-day statistics query. [day] is an ISO date such as 2026-11-27. */
data class DailyStat(val day: String, val total: Int, val correct: Int)

/** Practice on one calendar day (zero if the child did not play). */
data class DayActivity(val date: LocalDate, val answers: Int, val correct: Int)

object ActivityTimeline {

    /** The last [days] days ending today, oldest first, with zeros for days without practice. */
    fun lastDays(stats: List<DailyStat>, today: LocalDate, days: Int = 7): List<DayActivity> {
        val byDay = stats.associateBy { it.day }
        return (days - 1 downTo 0).map { offset ->
            val date = today.minusDays(offset.toLong())
            val stat = byDay[date.toString()]
            DayActivity(date, stat?.total ?: 0, stat?.correct ?: 0)
        }
    }
}
