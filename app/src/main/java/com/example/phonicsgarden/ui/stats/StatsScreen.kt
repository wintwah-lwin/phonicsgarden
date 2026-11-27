package com.example.phonicsgarden.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonicsgarden.engine.DayActivity
import com.example.phonicsgarden.engine.MasteryScheduler
import java.time.format.TextStyle as DayTextStyle
import java.util.Locale

/** Adult/Parent statistics screen protected by Parent Gate. */
@Composable
fun StatsScreen(
    onLock: () -> Unit = {},
    viewModel: StatsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StatsContent(state = state, onLock = onLock)
}

/** Stateless statistics UI (state hoisting), so it can be UI tested without a ViewModel. */
@Composable
fun StatsContent(state: StatsState, onLock: () -> Unit) {
    val accuracy = if (state.totalAttempts == 0) 0 else state.correctAttempts * 100 / state.totalAttempts

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
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
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("${state.totalAttempts} answers so far, $accuracy% correct")
                Text("Data stays on this device only.", style = MaterialTheme.typography.bodySmall)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Last 7 days", style = MaterialTheme.typography.titleMedium)
                WeeklyActivityChart(state.last7Days)
            }
        }
        if (state.practiceNext.isNotEmpty()) {
            item {
                Text(
                    "Practise next: ${state.practiceNext.joinToString(", ")}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        item { Text("All sounds", style = MaterialTheme.typography.titleMedium) }
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

/** Simple bar chart of answers per day. No streaks or targets, just a calm picture of practice. */
@Composable
fun WeeklyActivityChart(days: List<DayActivity>, modifier: Modifier = Modifier) {
    val maxAnswers = (days.maxOfOrNull { it.answers } ?: 0).coerceAtLeast(1)
    val summary = days.joinToString(", ") {
        "${it.date.dayOfWeek.getDisplayName(DayTextStyle.FULL, Locale.getDefault())}: ${it.answers}"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clearAndSetSemantics { contentDescription = "Answers in the last 7 days. $summary" },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEach { day ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    if (day.answers > 0) day.answers.toString() else "",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height((80f * day.answers / maxAnswers).coerceAtLeast(4f).dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                )
                Text(
                    day.date.dayOfWeek.getDisplayName(DayTextStyle.NARROW, Locale.getDefault()),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
