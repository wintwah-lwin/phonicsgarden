package com.example.phonicsgarden.ui.privacy

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** Adult screen reached from Settings (so it sits behind the Parent Gate). */
@Composable
fun PrivacyScreen(
    onBack: () -> Unit,
    viewModel: PrivacyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PrivacyContent(state = state, onDeleteConfirmed = viewModel::deleteAllProgress, onBack = onBack)
}

/** Stateless UI (state hoisting). Explains plainly what is kept, what leaves, and what is never done. */
@Composable
fun PrivacyContent(
    state: PrivacyState,
    onDeleteConfirmed: () -> Unit,
    onBack: () -> Unit
) {
    var confirmingDelete by remember { mutableStateOf(false) }
    var deleted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Back")
            }
        }
        Text("What we store 🔒", style = MaterialTheme.typography.headlineMedium)
        Text("Phonics Garden is built to keep as little as possible, and to keep it on this device.")

        InfoCard(
            title = "On this device only",
            lines = listOf(
                "${state.answersStored} answers (which sound, which game, right or wrong, and when)",
                "${state.soundsPractised} sounds practised, with a level for each",
                "Your settings, including the daily play time",
                "${state.downloadedWords} extra words downloaded from the word pack",
                "${state.playedTodayMinutes} minutes played today (used for the play limit)"
            )
        )

        InfoCard(
            title = "What leaves this device",
            lines = listOf(
                "The app asks GitHub for a public list of words. That request contains nothing about your child.",
                "Spoken prompts use the voice built into your device."
            )
        )

        InfoCard(
            title = "What we never do",
            lines = listOf(
                "No accounts, names or photos",
                "No ads",
                "No analytics or tracking",
                "No location, camera, contacts or microphone",
                "No cloud backups of your child's progress"
            )
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("You are in control", style = MaterialTheme.typography.titleMedium)
                Text("You can erase everything the app has learned about your child's practice at any time.")
                Button(
                    onClick = { confirmingDelete = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete all progress")
                }
                if (deleted) Text("Progress deleted.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    if (confirmingDelete) {
        AlertDialog(
            onDismissRequest = { confirmingDelete = false },
            title = { Text("Delete all progress?") },
            text = {
                Text(
                    "This removes every answer and sound level and starts the garden again. " +
                        "Settings and downloaded words stay. This cannot be undone."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmingDelete = false
                    deleted = true
                    onDeleteConfirmed()
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { confirmingDelete = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun InfoCard(title: String, lines: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            lines.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
        }
    }
}
