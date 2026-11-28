package com.example.phonicsgarden.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class AppSettings(
    val phase: Int = 1,
    val soundEnabled: Boolean = true,
    val reducedMotion: Boolean = false,
    /** Minutes of play per day before the app suggests a break. 0 means no limit. */
    val dailyLimitMinutes: Int = 0
)

private val Context.dataStore by preferencesDataStore(name = "settings")

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun setPhase(phase: Int)
    suspend fun setSoundEnabled(enabled: Boolean)
    suspend fun setReducedMotion(enabled: Boolean)
    suspend fun setDailyLimitMinutes(minutes: Int)
}

/** Stores parent/teacher preferences on the device using Preferences DataStore. */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    private object Keys {
        val PHASE = intPreferencesKey("phase")
        val SOUND = booleanPreferencesKey("sound_enabled")
        val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
        val DAILY_LIMIT = intPreferencesKey("daily_limit_minutes")
    }

    override val settings: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            phase = prefs[Keys.PHASE] ?: 1,
            soundEnabled = prefs[Keys.SOUND] ?: true,
            reducedMotion = prefs[Keys.REDUCED_MOTION] ?: false,
            dailyLimitMinutes = prefs[Keys.DAILY_LIMIT] ?: 0
        )
    }

    override suspend fun setPhase(phase: Int) {
        context.dataStore.edit { it[Keys.PHASE] = phase }
    }

    override suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SOUND] = enabled }
    }

    override suspend fun setReducedMotion(enabled: Boolean) {
        context.dataStore.edit { it[Keys.REDUCED_MOTION] = enabled }
    }

    override suspend fun setDailyLimitMinutes(minutes: Int) {
        context.dataStore.edit { it[Keys.DAILY_LIMIT] = minutes }
    }
}
