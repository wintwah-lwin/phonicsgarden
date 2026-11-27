package com.example.phonicsgarden.ui.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** Connects the landing page to its ViewModel. The UI itself lives in [LandingContent]. */
@Composable
fun LandingScreen(
    onBubblePop: () -> Unit,
    onFeedMonster: () -> Unit,
    onTracing: () -> Unit,
    viewModel: LandingViewModel = hiltViewModel()
) {
    val garden by viewModel.garden.collectAsStateWithLifecycle()
    LandingContent(
        garden = garden,
        onBubblePop = onBubblePop,
        onFeedMonster = onFeedMonster,
        onTracing = onTracing
    )
}

/** Stateless landing page UI (state hoisting), so it can be previewed and UI tested easily. */
@Composable
fun LandingContent(
    garden: GardenState,
    onBubblePop: () -> Unit,
    onFeedMonster: () -> Unit,
    onTracing: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "🐝 Phonics Garden",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text("Let's grow some sounds!", style = MaterialTheme.typography.titleLarge)

        GardenCard(garden)

        GameTile("🫧", "Bubble Pop", "Hear a sound and pop the letter", onBubblePop)
        GameTile("👾", "Feed the Monster", "Feed me words that start with a sound", onFeedMonster)
        GameTile("✏️", "Tracing Garden", "Trace a letter and watch it bloom", onTracing)
    }
}

@Composable
private fun GardenCard(garden: GardenState) {
    val growing = (garden.total - garden.mastered).coerceAtLeast(0)
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Your garden", style = MaterialTheme.typography.titleMedium)
            Text("🌸".repeat(garden.mastered) + "🌱".repeat(growing), fontSize = 32.sp)
            Text("${garden.mastered} of ${garden.total} sounds grown")
        }
    }
}

@Composable
private fun GameTile(emoji: String, title: String, subtitle: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 48.sp)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
