package com.example.phonicsgarden.ui.roundcomplete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** Connects the screen to its ViewModel. The UI itself lives in [RoundCompleteContent]. */
@Composable
fun RoundCompleteScreen(
    correct: Int,
    total: Int,
    onDone: () -> Unit,
    viewModel: RoundCompleteViewModel = hiltViewModel()
) {
    val limitReached by viewModel.limitReached.collectAsStateWithLifecycle()
    RoundCompleteContent(correct, total, limitReached, onDone)
}

/**
 * Natural stopping point: celebrate gently and suggest a break (no pressure to continue).
 * When the grown-up's daily limit is reached, the message becomes a calm "all done for today".
 */
@Composable
fun RoundCompleteContent(correct: Int, total: Int, limitReached: Boolean, onDone: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(if (limitReached) "🌙" else "🌻", fontSize = 96.sp)
        Text("Well done!", style = MaterialTheme.typography.displaySmall)
        Text("You got $correct of $total on the first try.", style = MaterialTheme.typography.titleLarge)
        Text(
            text = if (limitReached) {
                "That's all the playing for today. Your garden will be here tomorrow!"
            } else {
                "Time for a little break, or play again soon."
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Button(onClick = onDone) { Text("Back to the garden", fontSize = 20.sp) }
    }
}
