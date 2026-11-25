package com.example.phonicsgarden.ui.games.monster

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonicsgarden.data.local.WordPictures
import com.example.phonicsgarden.ui.games.Feedback
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FeedMonsterScreen(
    onFinished: (correct: Int, total: Int) -> Unit,
    viewModel: FeedMonsterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var monsterBounds by remember { mutableStateOf<Rect?>(null) }

    LaunchedEffect(state.finished) {
        if (state.finished) onFinished(state.correctCount, state.total)
    }

    // Monster "chomps" (grows briefly) when fed. Skipped if the parent chose reduced motion.
    val monsterScale by animateFloatAsState(
        targetValue = if (state.feedback == Feedback.CORRECT && !state.reducedMotion) 1.25f else 1f,
        label = "monsterScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("👾 Feed the Monster", style = MaterialTheme.typography.headlineMedium)

        if (state.noContent) {
            Text("No words to play with yet. Try another phase in Settings.", textAlign = TextAlign.Center)
            return@Column
        }

        if (state.total > 0) {
            LinearProgressIndicator(
                progress = { state.questionNumber.toFloat() / state.total },
                modifier = Modifier.fillMaxWidth()
            )
            Text("${state.questionNumber} of ${state.total}")
        }

        OutlinedButton(onClick = viewModel::replayPrompt) {
            Text("🔊 Hear it again", fontSize = 20.sp)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(180.dp)
                .onGloballyPositioned { monsterBounds = it.boundsInRoot() }
                .graphicsLayer { scaleX = monsterScale; scaleY = monsterScale }
                .clearAndSetSemantics { contentDescription = "A hungry monster" }
        ) {
            Text("👾", fontSize = 120.sp)
        }

        Text(
            text = when (state.feedback) {
                Feedback.CORRECT -> "Yum yum! 😋"
                Feedback.TRY_AGAIN -> "Hmm, not that one. Try another! 💛"
                null -> "Drag or tap a picture to feed me!"
            },
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        state.challenge?.let { challenge ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                challenge.options.forEach { word ->
                    // key() resets each card's drag position when the next question arrives.
                    key(state.questionNumber, word) {
                        PictureCard(
                            word = word,
                            enabled = !state.locked,
                            monsterBounds = monsterBounds,
                            onFed = { viewModel.onFed(word) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * A picture card that can be dragged onto the monster OR simply tapped (so children with
 * limited fine motor control, and TalkBack users, can still play).
 */
@Composable
private fun PictureCard(
    word: String,
    enabled: Boolean,
    monsterBounds: Rect?,
    onFed: () -> Unit
) {
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val scope = rememberCoroutineScope()
    var cardBounds by remember { mutableStateOf(Rect.Zero) }
    var dragging by remember { mutableStateOf(false) }

    val latestMonsterBounds by rememberUpdatedState(monsterBounds)
    val latestOnFed by rememberUpdatedState(onFed)

    fun release() {
        dragging = false
        val droppedOnMonster = latestMonsterBounds?.contains(cardBounds.center) == true
        if (droppedOnMonster) latestOnFed()
        scope.launch { offset.animateTo(Offset.Zero) } // always glide back to its spot
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 4.dp,
        shadowElevation = if (dragging) 12.dp else 4.dp,
        modifier = Modifier
            .size(width = 100.dp, height = 120.dp)
            .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) }
            .onGloballyPositioned { cardBounds = it.boundsInRoot() }
            .zIndex(if (dragging) 1f else 0f)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectDragGestures(
                    onDragStart = { dragging = true },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch { offset.snapTo(offset.value + dragAmount) }
                    },
                    onDragEnd = { release() },
                    onDragCancel = { release() }
                )
            }
            .clickable(enabled = enabled, onClickLabel = "Feed $word to the monster") { latestOnFed() }
            .semantics(mergeDescendants = true) { contentDescription = word }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(WordPictures.emojiFor(word) ?: "❓", fontSize = 48.sp)
            Text(word, style = MaterialTheme.typography.titleMedium)
        }
    }
}
