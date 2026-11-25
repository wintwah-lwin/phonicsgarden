package com.example.phonicsgarden.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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

private val phaseDescriptions = mapOf(
    1 to "Phase 1: s a t p i n",
    2 to "Phase 2: adds m d g o c k",
    3 to "Phase 3: adds e u r h b f"
)

/** Adult/Parent preferences screen protected by Parent Gate. */
@Composable
fun SettingsScreen(
    onLock: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                    onClick = { viewModel.setPhase(phase) },
                    label = { Text("Phase $phase") }
                )
            }
        }
        Text(phaseDescriptions[settings.phase] ?: "", style = MaterialTheme.typography.bodyMedium)

        SettingSwitch("Sound", settings.soundEnabled, viewModel::setSoundEnabled)
        SettingSwitch("Reduce motion", settings.reducedMotion, viewModel::setReducedMotion)
    }
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
