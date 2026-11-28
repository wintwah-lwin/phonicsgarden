package com.example.phonicsgarden.ui.games

import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.engine.ChallengeGenerator
import com.example.phonicsgarden.engine.WordChallengeGenerator
import com.example.phonicsgarden.testutil.FakeProgressRepository
import com.example.phonicsgarden.testutil.FakeSettingsRepository
import com.example.phonicsgarden.testutil.FakeSoundEffects
import com.example.phonicsgarden.testutil.FakeTtsSpeaker
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import com.example.phonicsgarden.ui.games.bubble.BubblePopViewModel
import com.example.phonicsgarden.ui.games.monster.FeedMonsterViewModel
import com.example.phonicsgarden.util.SoundEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** Checks which sound effect each game plays for each moment, and that Sound off silences them. */
@OptIn(ExperimentalCoroutinesApi::class)
class GameSoundEffectsTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val effects = FakeSoundEffects()

    private fun bubbleViewModel(settings: AppSettings = AppSettings()) = BubblePopViewModel(
        FakeWordRepository(), FakeProgressRepository(), FakeSettingsRepository(settings),
        ChallengeGenerator(), FakeTtsSpeaker(), effects
    )

    private fun monsterViewModel(settings: AppSettings = AppSettings()) = FeedMonsterViewModel(
        FakeWordRepository(), FakeProgressRepository(), FakeSettingsRepository(settings),
        WordChallengeGenerator(), FakeTtsSpeaker(), effects
    )

    private fun BubblePopViewModel.answerCorrectly() {
        val challenge = state.value.challenge!!
        onAnswer(challenge.options.first { challenge.isCorrect(it) })
    }

    private fun FeedMonsterViewModel.feedCorrectWord() {
        val challenge = state.value.challenge!!
        onFed(challenge.options.first { challenge.isCorrect(it) })
    }

    // ---------------- Bubble Pop ----------------

    @Test
    fun bubblePop_correctAnswer_pops() = runTest {
        val viewModel = bubbleViewModel()
        advanceUntilIdle()

        viewModel.answerCorrectly()

        assertEquals(listOf(SoundEffect.POP), effects.played)
    }

    @Test
    fun bubblePop_wrongAnswer_playsOnlyTheSoftSound() = runTest {
        val viewModel = bubbleViewModel()
        advanceUntilIdle()
        val challenge = viewModel.state.value.challenge!!

        viewModel.onAnswer(challenge.options.first { !challenge.isCorrect(it) })

        assertEquals(listOf(SoundEffect.SOFT), effects.played)
    }

    @Test
    fun bubblePop_finishingTheRound_endsWithAFanfare() = runTest {
        val viewModel = bubbleViewModel()
        advanceUntilIdle()

        repeat(BubblePopViewModel.ROUND_LENGTH) {
            viewModel.answerCorrectly()
            advanceUntilIdle()
        }

        assertEquals(SoundEffect.FANFARE, effects.played.last())
        assertEquals(1, effects.played.count { it == SoundEffect.FANFARE })
    }

    @Test
    fun bubblePop_soundOff_isSilent() = runTest {
        val viewModel = bubbleViewModel(AppSettings(soundEnabled = false))
        advanceUntilIdle()

        viewModel.answerCorrectly()
        advanceUntilIdle()

        assertTrue(effects.played.isEmpty())
    }

    // ---------------- Feed the Monster ----------------

    @Test
    fun feedMonster_correctWord_chomps() = runTest {
        val viewModel = monsterViewModel()
        advanceUntilIdle()

        viewModel.feedCorrectWord()

        assertEquals(listOf(SoundEffect.CHOMP), effects.played)
    }

    @Test
    fun feedMonster_wrongWord_playsOnlyTheSoftSound() = runTest {
        val viewModel = monsterViewModel()
        advanceUntilIdle()
        val challenge = viewModel.state.value.challenge!!

        viewModel.onFed(challenge.options.first { !challenge.isCorrect(it) })

        assertEquals(listOf(SoundEffect.SOFT), effects.played)
    }

    @Test
    fun feedMonster_finishingTheRound_endsWithAFanfare() = runTest {
        val viewModel = monsterViewModel()
        advanceUntilIdle()

        repeat(FeedMonsterViewModel.ROUND_LENGTH) {
            viewModel.feedCorrectWord()
            advanceUntilIdle()
        }

        assertEquals(SoundEffect.FANFARE, effects.played.last())
    }

    @Test
    fun feedMonster_soundOff_isSilent() = runTest {
        val viewModel = monsterViewModel(AppSettings(soundEnabled = false))
        advanceUntilIdle()

        viewModel.feedCorrectWord()
        advanceUntilIdle()

        assertTrue(effects.played.isEmpty())
    }
}
