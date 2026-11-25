package com.example.phonicsgarden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.phonicsgarden.ui.navigation.AppNavHost
import com.example.phonicsgarden.ui.theme.PhonicsGardenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhonicsGardenTheme {
                AppNavHost()
            }
        }
    }
}
