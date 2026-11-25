package com.example.phonicsgarden.ui.games.tracing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonicsgarden.engine.TracePoint
import com.example.phonicsgarden.ui.games.Feedback

@Composable
fun TracingScreen(
    onFinished: (correct: Int, total: Int) -> Unit,
    viewModel: TracingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.finished) {
        if (state.finished) onFinished(state.correctCount, state.total)
    }

    // A flower blooms over the letter when it is traced correctly (instant if reduce motion is on).
    val bloom by animateFloatAsState(
        targetValue = if (state.feedback == Feedback.CORRECT) 1f else 0f,
        animationSpec = if (state.reducedMotion) snap() else spring(),
        label = "bloom"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("✏️ Tracing Garden", style = MaterialTheme.typography.headlineMedium)

        if (state.noContent) {
            Text("No letters to trace yet. Try another phase in Settings.", textAlign = TextAlign.Center)
            return@Column
        }

        if (state.total > 0) {
            LinearProgressIndicator(
                progress = { state.questionNumber.toFloat() / state.total },
                modifier = Modifier.fillMaxWidth()
            )
            Text("${state.questionNumber} of ${state.total}")
        }

        Text(
            text = when (state.feedback) {
                Feedback.CORRECT -> "Beautiful writing! 🌟"
                Feedback.TRY_AGAIN -> "Nearly! Follow the dots from the start 💛"
                null -> "Trace the letter: ${state.letter}"
            },
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Box(contentAlignment = Alignment.Center) {
            TracingCanvas(
                // Reset in-progress drawing whenever the letter changes.
                key = state.letter,
                guides = state.guides,
                completedStrokes = state.completedStrokes,
                locked = state.locked,
                onStrokeFinished = viewModel::onStrokeFinished,
                letter = state.letter
            )
            Text(
                "🌸",
                fontSize = 120.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = bloom
                    scaleY = bloom
                    alpha = bloom
                }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = viewModel::replayPrompt) { Text("🔊 Hear it", fontSize = 18.sp) }
            OutlinedButton(onClick = viewModel::startAgain, enabled = !state.locked) {
                Text("Start again", fontSize = 18.sp)
            }
            TextButton(onClick = viewModel::skip, enabled = !state.locked) { Text("Skip", fontSize = 18.sp) }
        }
    }
}

@Composable
private fun TracingCanvas(
    key: String,
    guides: List<List<TracePoint>>,
    completedStrokes: List<List<TracePoint>>,
    locked: Boolean,
    onStrokeFinished: (List<TracePoint>) -> Unit,
    letter: String
) {
    // The in-progress stroke (pixels). Cleared per letter via the remember key.
    val current = remember(key) { mutableStateListOf<Offset>() }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
    val inkColor = MaterialTheme.colorScheme.primary
    val lineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    val startColors = listOf(Color(0xFF2E7D32), Color(0xFFF9A825), Color(0xFF8E24AA))

    fun finishStroke() {
        val w = canvasSize.width.toFloat()
        val h = canvasSize.height.toFloat()
        if (w > 0f && h > 0f && current.isNotEmpty()) {
            onStrokeFinished(current.map { TracePoint(it.x / w, it.y / h) })
        }
        current.clear()
    }

    Canvas(
        modifier = Modifier
            .sizeIn(maxWidth = 360.dp, maxHeight = 360.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .onSizeChanged { canvasSize = it }
            .semantics { contentDescription = "Drawing area. Trace the letter $letter with your finger." }
            .pointerInput(locked, key) {
                if (locked) return@pointerInput
                detectDragGestures(
                    onDragStart = { start -> current.clear(); current.add(start) },
                    onDrag = { change, _ ->
                        change.consume()
                        current.add(change.position)
                    },
                    onDragEnd = { finishStroke() },
                    onDragCancel = { current.clear() }
                )
            }
            .pointerInput(locked, key) {
                if (locked) return@pointerInput
                // A tap draws a single dot (needed for the dot on "i").
                detectTapGestures(onTap = { position ->
                    current.clear()
                    current.add(position)
                    finishStroke()
                })
            }
    ) {
        val w = size.width
        val h = size.height
        fun px(p: TracePoint) = Offset(p.x * w, p.y * h)

        // Writing lines: x-height and baseline.
        listOf(0.35f, 0.75f).forEach { y ->
            drawLine(
                color = lineColor,
                start = Offset(0f, y * h),
                end = Offset(w, y * h),
                strokeWidth = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 14f))
            )
        }

        // Guide track the child follows, with a coloured dot where each stroke starts.
        val trackWidth = w * 0.12f
        guides.forEach { stroke ->
            val points = stroke.map(::px)
            if (points.size == 1) {
                drawCircle(trackColor, radius = trackWidth / 2f, center = points[0])
            } else {
                drawPath(
                    path = smoothPath(points),
                    color = trackColor,
                    style = Stroke(width = trackWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
        }
        guides.forEachIndexed { i, stroke ->
            drawCircle(startColors[i % startColors.size], radius = trackWidth * 0.35f, center = px(stroke.first()))
        }

        // The child's ink: finished strokes, then the stroke in progress.
        val inkWidth = w * 0.06f
        completedStrokes.forEach { drawInk(it.map(::px), inkColor, inkWidth) }
        drawInk(current.toList(), inkColor, inkWidth)
    }
}

private fun DrawScope.drawInk(points: List<Offset>, color: Color, width: Float) {
    when {
        points.isEmpty() -> Unit
        points.size == 1 -> drawCircle(color, radius = width / 2f, center = points[0])
        else -> drawPath(
            path = Path().apply {
                moveTo(points[0].x, points[0].y)
                for (i in 1 until points.size) lineTo(points[i].x, points[i].y)
            },
            color = color,
            style = Stroke(width = width, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/** Rounds off the corners between waypoints so curved letters look curved. */
private fun smoothPath(points: List<Offset>): Path = Path().apply {
    moveTo(points[0].x, points[0].y)
    for (i in 1 until points.size - 1) {
        val mid = Offset((points[i].x + points[i + 1].x) / 2f, (points[i].y + points[i + 1].y) / 2f)
        quadraticBezierTo(points[i].x, points[i].y, mid.x, mid.y)
    }
    lineTo(points.last().x, points.last().y)
}
