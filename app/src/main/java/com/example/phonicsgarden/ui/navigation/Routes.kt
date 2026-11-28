package com.example.phonicsgarden.ui.navigation

import kotlinx.serialization.Serializable

// Type-safe Navigation Compose routes (Navigation 2.8+).
@Serializable object Landing
@Serializable object BubblePop
@Serializable object FeedMonster
@Serializable object Tracing
@Serializable data class RoundComplete(val correct: Int, val total: Int)
@Serializable object Stats
@Serializable object Settings
@Serializable object Privacy
