package com.example.phonicsgarden.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonicsgarden.engine.MasteryScheduler

/** Adult/Parent statistics screen protected by Parent Gate. */
@Composable
fun StatsScreen(
    onLock: () -> Unit = {},
    viewModel: StatsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val accuracy = if (state.totalAttempts == 0) 0 else state.correctAttempts * 100 / state.totalAttempts

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Progress 🔒", style = MaterialTheme.typography.headlineMedium)
            OutlinedButton(onClick = onLock) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Lock Gate")
            }
        }
        Text("${state.totalAttempts} answers so far, $accuracy% correct")
        Text("Data stays on this device only.", style = MaterialTheme.typography.bodySmall)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.sounds) { sound ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(sound.letter, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.width(40.dp))
                    LinearProgressIndicator(
                        progress = { sound.box.toFloat() / MasteryScheduler.MAX_BOX },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(if (MasteryScheduler.isMastered(sound.box)) "Grown 🌸" else "Growing 🌱")
                }
            }
        }
    }
}
