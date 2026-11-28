package com.example.phonicsgarden.ui.settings

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.engine.PlayLimit

private val phaseDescriptions = mapOf(
    1 to "Phase 1: s a t p i n",
    2 to "Phase 2: adds m d g o c k",
    3 to "Phase 3: adds e u r h b f"
)

/** Adult/Parent preferences screen protected by Parent Gate. */
@Composable
fun SettingsScreen(
    onLock: () -> Unit = {},
    onOpenPrivacy: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val downloadedWords by viewModel.downloadedWords.collectAsStateWithLifecycle()
    val syncStatus by viewModel.syncStatus.collectAsStateWithLifecycle()
    val playedToday by viewModel.playedTodayMinutes.collectAsStateWithLifecycle()
    SettingsContent(
        settings = settings,
        onPhaseSelected = viewModel::setPhase,
        onSoundChanged = viewModel::setSoundEnabled,
        onReducedMotionChanged = viewModel::setReducedMotion,
        onLock = onLock,
        downloadedWords = downloadedWords,
        syncStatus = syncStatus,
        onCheckForWords = viewModel::checkForNewWords,
        playedTodayMinutes = playedToday,
        onDailyLimitSelected = viewModel::setDailyLimitMinutes,
        onOpenPrivacy = onOpenPrivacy
    )
}

/** Stateless settings UI (state hoisting), so it can be UI tested without a ViewModel. */
@Composable
fun SettingsContent(
    settings: AppSettings,
    onPhaseSelected: (Int) -> Unit,
    onSoundChanged: (Boolean) -> Unit,
    onReducedMotionChanged: (Boolean) -> Unit,
    onLock: () -> Unit,
    downloadedWords: Int = 0,
    syncStatus: SyncStatus = SyncStatus.Idle,
    onCheckForWords: () -> Unit = {},
    playedTodayMinutes: Int = 0,
    onDailyLimitSelected: (Int) -> Unit = {},
    onOpenPrivacy: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Settings 🔒", style = MaterialTheme.typography.headlineMedium)
            OutlinedButton(onClick = onLock) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Lock Gate")
            }
        }

        Text("Letters and sounds", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            phaseDescriptions.keys.forEach { phase ->
                FilterChip(
                    selected = settings.phase == phase,
                    onClick = { onPhaseSelected(phase) },
                    label = { Text("Phase $phase") }
                )
            }
        }
        Text(phaseDescriptions[settings.phase] ?: "", style = MaterialTheme.typography.bodyMedium)

        SettingSwitch("Sound", settings.soundEnabled, onSoundChanged)
        SettingSwitch(
            label = "Reduce motion",
            checked = settings.reducedMotion,
            onChange = onReducedMotionChanged,
            description = "Bubbles stay still instead of floating."
        )

        DailyLimitCard(settings.dailyLimitMinutes, playedTodayMinutes, onDailyLimitSelected)

        WordPackCard(downloadedWords, syncStatus, onCheckForWords)

        OutlinedButton(onClick = onOpenPrivacy, modifier = Modifier.fillMaxWidth()) {
            Text("What we store")
        }
    }
}

/**
 * A kind limit, set by the grown-up. The child never sees a timer: when the limit is reached the
 * games rest until tomorrow, and a round is never cut off halfway.
 */
@Composable
private fun DailyLimitCard(limitMinutes: Int, playedTodayMinutes: Int, onSelected: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Daily play time", style = MaterialTheme.typography.titleMedium)
            Text(
                "When this much has been played today, the games rest until tomorrow. " +
                    "A round is never stopped halfway.",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlayLimit.OPTIONS_MINUTES.forEach { minutes ->
                    FilterChip(
                        selected = limitMinutes == minutes,
                        onClick = { onSelected(minutes) },
                        label = { Text(if (minutes == 0) "Off" else "$minutes min") }
                    )
                }
            }
            Text("Played today: $playedTodayMinutes min", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/** Lets a grown-up download new words. Only a public word list is fetched; nothing is uploaded. */
@Composable
private fun WordPackCard(downloadedWords: Int, status: SyncStatus, onCheck: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Word pack", style = MaterialTheme.typography.titleMedium)
            Text("$downloadedWords extra words downloaded")
            Text(syncStatusMessage(status), style = MaterialTheme.typography.bodyMedium)
            Button(onClick = onCheck, enabled = status != SyncStatus.Checking) {
                Text("Check for new words")
            }
        }
    }
}

private fun syncStatusMessage(status: SyncStatus): String = when (status) {
    SyncStatus.Idle -> "New words are checked when the app starts."
    SyncStatus.Checking -> "Checking for new words..."
    is SyncStatus.Updated -> "Added ${status.added} new words!"
    SyncStatus.UpToDate -> "You're up to date."
    SyncStatus.Offline -> "Couldn't reach the word list. The built-in words still work."
}

@Composable
private fun SettingSwitch(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    description: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            if (description != null) Text(description, style = MaterialTheme.typography.bodySmall)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
