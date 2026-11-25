package com.example.phonicsgarden.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.phonicsgarden.data.local.entity.Attempt
import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Mastery
import com.example.phonicsgarden.data.local.entity.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface GraphemeDao {
    @Query("SELECT * FROM graphemes ORDER BY id")
    fun observeAll(): Flow<List<Grapheme>>

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

    @Query("SELECT COUNT(*) FROM words")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<Word>)
}

@Dao
interface AttemptDao {
    @Insert
    suspend fun insert(attempt: Attempt)

    @Query("SELECT COUNT(*) FROM attempts")
    fun observeTotal(): Flow<Int>

    @Query("SELECT COUNT(*) FROM attempts WHERE correct = 1")
    fun observeCorrect(): Flow<Int>
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
}
