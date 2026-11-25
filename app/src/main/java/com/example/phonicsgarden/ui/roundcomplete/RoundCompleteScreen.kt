package com.example.phonicsgarden.ui.roundcomplete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Natural stopping point: celebrate gently and suggest a break (no pressure to continue). */
@Composable
fun RoundCompleteScreen(correct: Int, total: Int, onDone: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text("🌻", fontSize = 96.sp)
        Text("Well done!", style = MaterialTheme.typography.displaySmall)
        Text("You got $correct of $total on the first try.", style = MaterialTheme.typography.titleLarge)
        Text("Time for a little break, or play again soon.", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onDone) { Text("Back to the garden", fontSize = 20.sp) }
    }
}
