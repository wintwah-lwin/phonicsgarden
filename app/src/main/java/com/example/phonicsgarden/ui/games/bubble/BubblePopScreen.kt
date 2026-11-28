package com.example.phonicsgarden.ui.games.bubble

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonicsgarden.engine.BubbleField
import com.example.phonicsgarden.engine.Challenge
import com.example.phonicsgarden.ui.games.Feedback
import kotlin.math.roundToInt
import kotlin.random.Random

private val BubbleDiameter = 104.dp

/** One full journey from the bottom of the field to the top. Slow and gentle for small children. */
private const val FLOAT_PERIOD_MS = 16_000

// Friendly colours that all keep white letters easy to read.
private val BubbleColors = listOf(
    Color(0xFF00897B), Color(0xFF1E88E5), Color(0xFF8E24AA), Color(0xFFEF6C00)
)

@Composable
fun BubblePopScreen(
    onFinished: (correct: Int, total: Int) -> Unit,
    viewModel: BubblePopViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Moving targets are hard with a screen reader, so treat it like "Reduce motion".
    val screenReaderOn = remember {
        (context.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager)
            ?.isTouchExplorationEnabled == true
    }

    LaunchedEffect(state.finished) {
        if (state.finished) onFinished(state.correctCount, state.total)
    }

    BubblePopContent(
        state = state,
        floating = !state.reducedMotion && !screenReaderOn,
        onReplaySound = viewModel::replaySound,
        onAnswer = viewModel::onAnswer
    )
}

/** Stateless Bubble Pop UI (state hoisting), so it can be UI tested without a ViewModel. */
@Composable
fun BubblePopContent(
    state: BubblePopState,
    floating: Boolean,
    onReplaySound: () -> Unit,
    onAnswer: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🫧 Bubble Pop", style = MaterialTheme.typography.headlineMedium)

        if (state.total > 0) {
            LinearProgressIndicator(
                progress = { state.questionNumber.toFloat() / state.total },
                modifier = Modifier.fillMaxWidth()
            )
            Text("${state.questionNumber} of ${state.total}")
        }

        OutlinedButton(onClick = onReplaySound) {
            Text("🔊 Hear it again", fontSize = 20.sp)
        }

        Text(
            text = when (state.feedback) {
                Feedback.CORRECT -> "Great job! 🌟"
                Feedback.TRY_AGAIN -> "Try again! 💛"
                null -> "Pop the letter you hear!"
            },
            style = MaterialTheme.typography.titleLarge
        )

        state.challenge?.let { challenge ->
            if (floating) {
                FloatingBubbleField(
                    challenge = challenge,
                    onAnswer = onAnswer,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            } else {
                StaticBubbleGrid(challenge = challenge, onAnswer = onAnswer)
            }
        }
    }
}

/** Bubbles that hold still in a grid. Used for "Reduce motion" and screen reader users. */
@Composable
private fun StaticBubbleGrid(challenge: Challenge, onAnswer: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(challenge.options) { letter ->
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { onAnswer(letter) },
                    shape = CircleShape,
                    modifier = Modifier
                        .size(140.dp)
                        .semantics { contentDescription = "Letter $letter" }
                ) {
                    Text(letter, fontSize = 56.sp)
                }
            }
        }
    }
}

/** Letters drift slowly upwards. Each new question starts a fresh field with fresh bubbles. */
@Composable
private fun FloatingBubbleField(
    challenge: Challenge,
    onAnswer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    key(challenge.graphemeId) {
        FloatingBubbles(challenge, onAnswer, modifier)
    }
}

@Composable
private fun FloatingBubbles(
    challenge: Challenge,
    onAnswer: (String) -> Unit,
    modifier: Modifier
) {
    val specs = remember(challenge.graphemeId) {
        BubbleField.layout(challenge.options.size, Random(challenge.graphemeId))
    }
    val transition = rememberInfiniteTransition(label = "bubbleFloat")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(FLOAT_PERIOD_MS, easing = LinearEasing), RepeatMode.Restart),
        label = "bubbleProgress"
    )

    BoxWithConstraints(modifier = modifier.clipToBounds()) {
        val fieldWidth = constraints.maxWidth
        val fieldHeight = constraints.maxHeight

        challenge.options.forEachIndexed { index, letter ->
            key(letter) {
                FloatingBubble(
                    letter = letter,
                    color = BubbleColors[index % BubbleColors.size],
                    isCorrect = challenge.isCorrect(letter),
                    onTap = { onAnswer(letter) },
                    // The position is read while laying out, so moving bubbles never
                    // trigger a recomposition. Hit testing follows the moving bubble.
                    modifier = Modifier.offset {
                        val diameter = BubbleDiameter.roundToPx()
                        val spec = specs[index]
                        val centerX = BubbleField.horizontalCenter(spec, progress) * fieldWidth
                        val x = (centerX - diameter / 2f).coerceIn(0f, (fieldWidth - diameter).coerceAtLeast(0).toFloat())
                        val travel = fieldHeight + diameter
                        val y = fieldHeight - BubbleField.verticalFraction(spec, progress) * travel
                        IntOffset(x.roundToInt(), y.roundToInt())
                    }
                )
            }
        }
    }
}

@Composable
private fun FloatingBubble(
    letter: String,
    color: Color,
    isCorrect: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    var popped by remember { mutableStateOf(false) }
    var wobbles by remember { mutableIntStateOf(0) }

    // Correct: the bubble swells and fades like a pop. Not quite: it gives a small friendly wobble.
    val scale by animateFloatAsState(if (popped) 1.5f else 1f, tween(220), label = "popScale")
    val alpha by animateFloatAsState(if (popped) 0f else 1f, tween(220), label = "popAlpha")
    val shake = remember { Animatable(0f) }
    LaunchedEffect(wobbles) {
        if (wobbles > 0) {
            shake.animateTo(10f, tween(60))
            shake.animateTo(-10f, tween(120))
            shake.animateTo(0f, tween(60))
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(BubbleDiameter)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                rotationZ = shake.value
            }
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(color.copy(alpha = 0.75f), color)))
            .border(3.dp, Color.White.copy(alpha = 0.7f), CircleShape)
            .clickable(enabled = !popped, role = Role.Button, onClickLabel = "Pop letter $letter") {
                if (isCorrect) popped = true else wobbles++
                onTap()
            }
            .semantics { contentDescription = "Letter $letter" }
    ) {
        Text(letter, fontSize = 52.sp, fontWeight = FontWeight.Bold, color = Color.White)
        // A small shine so it reads as a bubble.
        Box(
            Modifier
                .align(Alignment.TopStart)
                .padding(start = 22.dp, top = 14.dp)
                .size(18.dp)
                .background(Color.White.copy(alpha = 0.45f), CircleShape)
        )
    }
}
