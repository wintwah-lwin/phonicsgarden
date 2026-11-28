package com.example.phonicsgarden.data.repository

import androidx.room.withTransaction
import com.example.phonicsgarden.data.local.AppDatabase
import com.example.phonicsgarden.data.local.dao.AttemptDao
import com.example.phonicsgarden.data.local.dao.MasteryDao
import com.example.phonicsgarden.data.local.entity.Attempt
import com.example.phonicsgarden.data.local.entity.Mastery
import com.example.phonicsgarden.engine.ActivityTimeline
import com.example.phonicsgarden.engine.DayActivity
import com.example.phonicsgarden.engine.MasteryScheduler
import com.example.phonicsgarden.engine.MasteryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

data class AttemptSummary(val total: Int, val correct: Int)

interface ProgressRepository {
    /** Map of graphemeId -> Leitner box. */
    fun observeMasteryBoxes(): Flow<Map<Long, Int>>
    suspend fun masteryBoxes(): Map<Long, Int>
    suspend fun recordAttempt(graphemeId: Long, game: String, correct: Boolean)
    fun observeAttemptSummary(): Flow<AttemptSummary>

    /** Answers per day for the last 7 days (oldest first), including days with no practice. */
    fun observeLastSevenDays(): Flow<List<DayActivity>>

    /** Erases every answer and sound level (the garden starts again). Settings are kept. */
    suspend fun deleteAllProgress()
}

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val attemptDao: AttemptDao,
    private val masteryDao: MasteryDao
) : ProgressRepository {

    override fun observeMasteryBoxes(): Flow<Map<Long, Int>> =
        masteryDao.observeAll().map { list -> list.associate { it.graphemeId to it.box } }

    override suspend fun masteryBoxes(): Map<Long, Int> =
        masteryDao.getAll().associate { it.graphemeId to it.box }

    override suspend fun recordAttempt(graphemeId: Long, game: String, correct: Boolean) {
        val now = System.currentTimeMillis()
        attemptDao.insert(Attempt(graphemeId = graphemeId, game = game, correct = correct, timestamp = now))

        val current = masteryDao.get(graphemeId)
        val next = MasteryScheduler.next(
            current = MasteryState(current?.box ?: 0, current?.nextReviewAt ?: 0L),
            correct = correct,
            now = now
        )
        masteryDao.upsert(Mastery(graphemeId, next.box, next.nextReviewAt))
    }

    override fun observeAttemptSummary(): Flow<AttemptSummary> =
        combine(attemptDao.observeTotal(), attemptDao.observeCorrect()) { total, correct ->
            AttemptSummary(total, correct)
        }

    override suspend fun deleteAllProgress() {
        // One transaction, so progress is never left half deleted.
        database.withTransaction {
            attemptDao.deleteAll()
            masteryDao.deleteAll()
        }
    }

    override fun observeLastSevenDays(): Flow<List<DayActivity>> {
        val today = LocalDate.now()
        val since = today.minusDays(6).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return attemptDao.observeDailyStats(since).map { ActivityTimeline.lastDays(it, today) }
    }
}
