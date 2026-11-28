package com.example.phonicsgarden.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.phonicsgarden.data.local.entity.Attempt
import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Mastery
import com.example.phonicsgarden.data.local.entity.Word
import com.example.phonicsgarden.engine.DailyStat
import kotlinx.coroutines.flow.Flow

@Dao
interface GraphemeDao {
    @Query("SELECT * FROM graphemes ORDER BY id")
    fun observeAll(): Flow<List<Grapheme>>

    @Query("SELECT * FROM graphemes ORDER BY id")
    suspend fun getAll(): List<Grapheme>

    @Query("SELECT * FROM graphemes WHERE phase <= :phase ORDER BY id")
    suspend fun getUpToPhase(phase: Int): List<Grapheme>

    @Query("SELECT COUNT(*) FROM graphemes")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Grapheme>)
}

@Dao
interface WordDao {
    @Query("SELECT * FROM words WHERE firstLetter = :letter")
    suspend fun byFirstLetter(letter: String): List<Word>

    @Query("SELECT * FROM words WHERE phase <= :phase")
    suspend fun upToPhase(phase: Int): List<Word>

    @Query("SELECT COUNT(*) FROM words WHERE source = :source")
    fun observeCountBySource(source: String): Flow<Int>

    /** Words that already exist are ignored. Returns the new row id, or -1 for an ignored word. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<Word>): List<Long>
}

@Dao
interface AttemptDao {
    @Insert
    suspend fun insert(attempt: Attempt)

    @Query("SELECT COUNT(*) FROM attempts")
    fun observeTotal(): Flow<Int>

    @Query("SELECT COUNT(*) FROM attempts WHERE correct = 1")
    fun observeCorrect(): Flow<Int>

    /** Answers per local calendar day since [since] (epoch millis), oldest first. */
    @Query(
        """
        SELECT date(timestamp / 1000, 'unixepoch', 'localtime') AS day,
               COUNT(*) AS total,
               SUM(correct) AS correct
        FROM attempts
        WHERE timestamp >= :since
        GROUP BY day
        ORDER BY day
        """
    )
    fun observeDailyStats(since: Long): Flow<List<DailyStat>>

    @Query("DELETE FROM attempts")
    suspend fun deleteAll()
}

@Dao
interface MasteryDao {
    @Query("SELECT * FROM mastery")
    fun observeAll(): Flow<List<Mastery>>

    @Query("SELECT * FROM mastery")
    suspend fun getAll(): List<Mastery>

    @Query("SELECT * FROM mastery WHERE graphemeId = :id")
    suspend fun get(id: Long): Mastery?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(mastery: Mastery)

    @Query("DELETE FROM mastery")
    suspend fun deleteAll()
}
