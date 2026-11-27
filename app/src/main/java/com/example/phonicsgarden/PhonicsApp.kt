package com.example.phonicsgarden

import android.app.Application
import com.example.phonicsgarden.data.repository.WordRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class PhonicsApp : Application() {

    @Inject lateinit var wordRepository: WordRepository

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Seed the local DB first so the app works offline, then try to enrich it from the network.
        appScope.launch {
            wordRepository.seedIfEmpty()
            wordRepository.syncWordPack()
        }
    }
}
