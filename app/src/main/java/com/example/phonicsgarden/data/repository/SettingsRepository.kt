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
    val reducedMotion: Boolean = false
)

private val Context.dataStore by preferencesDataStore(name = "settings")

/** Stores parent/teacher preferences on the device using Preferences DataStore. */
@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val PHASE = intPreferencesKey("phase")
        val SOUND = booleanPreferencesKey("sound_enabled")
        val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
    }

    val settings: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            phase = prefs[Keys.PHASE] ?: 1,
            soundEnabled = prefs[Keys.SOUND] ?: true,
            reducedMotion = prefs[Keys.REDUCED_MOTION] ?: false
        )
    }

    suspend fun setPhase(phase: Int) {
        context.dataStore.edit { it[Keys.PHASE] = phase }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SOUND] = enabled }
    }

    suspend fun setReducedMotion(enabled: Boolean) {
        context.dataStore.edit { it[Keys.REDUCED_MOTION] = enabled }
    }
}
