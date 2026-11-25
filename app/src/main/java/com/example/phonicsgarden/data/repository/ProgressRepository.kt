package com.example.phonicsgarden.data.repository

import com.example.phonicsgarden.data.local.dao.AttemptDao
import com.example.phonicsgarden.data.local.dao.MasteryDao
import com.example.phonicsgarden.data.local.entity.Attempt
import com.example.phonicsgarden.data.local.entity.Mastery
import com.example.phonicsgarden.engine.MasteryScheduler
import com.example.phonicsgarden.engine.MasteryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class AttemptSummary(val total: Int, val correct: Int)

interface ProgressRepository {
    /** Map of graphemeId -> Leitner box. */
    fun observeMasteryBoxes(): Flow<Map<Long, Int>>
    suspend fun masteryBoxes(): Map<Long, Int>
    suspend fun recordAttempt(graphemeId: Long, game: String, correct: Boolean)
    fun observeAttemptSummary(): Flow<AttemptSummary>
}

@Singleton
class ProgressRepositoryImpl @Inject constructor(
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
}
