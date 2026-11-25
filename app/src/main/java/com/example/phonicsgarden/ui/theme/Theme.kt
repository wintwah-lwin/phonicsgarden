package com.example.phonicsgarden.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Fixed, high-contrast, friendly palette (dynamic colour is off so the look is consistent).
private val GardenColors = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    secondary = Color(0xFFF9A825),
    onSecondary = Color(0xFF1B1B1B),
    tertiary = Color(0xFF8E24AA),
    background = Color(0xFFF1F8E9),
    surface = Color.White
)

@Composable
fun PhonicsGardenTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = GardenColors, content = content)
}
