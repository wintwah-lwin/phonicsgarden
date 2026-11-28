package com.example.phonicsgarden.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/** How long the child has played today. Stored only on the device. */
interface PlayTimeRepository {
    fun observePlayedTodayMillis(): Flow<Long>
    suspend fun addPlayTime(millis: Long)
    suspend fun resetToday()
}

private val Context.playTimeDataStore by preferencesDataStore(name = "play_time")

@Singleton
class PlayTimeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PlayTimeRepository {

    private object Keys {
        val DAY = stringPreferencesKey("day")
        val MILLIS = longPreferencesKey("millis")
    }

    /** Only today's total counts: a total saved on an earlier day reads as zero. */
    override fun observePlayedTodayMillis(): Flow<Long> =
        context.playTimeDataStore.data.map { prefs ->
            if (prefs[Keys.DAY] == LocalDate.now().toString()) prefs[Keys.MILLIS] ?: 0L else 0L
        }

    override suspend fun addPlayTime(millis: Long) {
        if (millis <= 0L) return
        context.playTimeDataStore.edit { prefs ->
            val today = LocalDate.now().toString()
            val soFar = if (prefs[Keys.DAY] == today) prefs[Keys.MILLIS] ?: 0L else 0L
            prefs[Keys.DAY] = today
            prefs[Keys.MILLIS] = soFar + millis
        }
    }

    override suspend fun resetToday() {
        context.playTimeDataStore.edit { it.clear() }
    }
}
