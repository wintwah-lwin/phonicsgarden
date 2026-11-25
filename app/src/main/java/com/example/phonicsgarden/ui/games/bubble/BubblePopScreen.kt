package com.example.phonicsgarden.ui.games.bubble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonicsgarden.ui.games.Feedback

@Composable
fun BubblePopScreen(
    onFinished: (correct: Int, total: Int) -> Unit,
    viewModel: BubblePopViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.finished) {
        if (state.finished) onFinished(state.correctCount, state.total)
    }

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

        OutlinedButton(onClick = viewModel::replaySound) {
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(challenge.options) { letter ->
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { viewModel.onAnswer(letter) },
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
    }
}
