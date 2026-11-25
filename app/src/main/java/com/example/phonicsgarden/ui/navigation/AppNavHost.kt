package com.example.phonicsgarden.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.phonicsgarden.ui.parentgate.ParentGateDialog
import com.example.phonicsgarden.ui.games.bubble.BubblePopScreen
import com.example.phonicsgarden.ui.games.monster.FeedMonsterScreen
import com.example.phonicsgarden.ui.games.tracing.TracingScreen
import com.example.phonicsgarden.ui.landing.LandingScreen
import com.example.phonicsgarden.ui.roundcomplete.RoundCompleteScreen
import com.example.phonicsgarden.ui.settings.SettingsScreen
import com.example.phonicsgarden.ui.stats.StatsScreen

private data class TopLevelItem(
    val label: String,
    val route: Any,
    val icon: ImageVector,
    val selected: Boolean
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    var isParentUnlocked by remember { mutableStateOf(false) }
    var pendingRoute by remember { mutableStateOf<Any?>(null) }

    val items = listOf(
        TopLevelItem("Garden", Landing, Icons.Default.Home, destination?.hasRoute<Landing>() == true),
        TopLevelItem("Progress", Stats, Icons.Default.Star, destination?.hasRoute<Stats>() == true),
        TopLevelItem("Settings", Settings, Icons.Default.Settings, destination?.hasRoute<Settings>() == true)
    )
    // Bottom bar only on top-level screens; games use the full screen.
    val showBottomBar = items.any { it.selected }

    fun navigateToRoute(route: Any) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    pendingRoute?.let { targetRoute ->
        ParentGateDialog(
            onSuccess = {
                pendingRoute = null
                isParentUnlocked = true
                navigateToRoute(targetRoute)
            },
            onDismiss = {
                pendingRoute = null
            }
        )
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = item.selected,
                            onClick = {
                                val isProtected = item.route is Stats || item.route is Settings
                                if (isProtected && !isParentUnlocked) {
                                    pendingRoute = item.route
                                } else {
                                    navigateToRoute(item.route)
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Landing,
            modifier = Modifier.padding(padding)
        ) {
            composable<Landing> {
                LandingScreen(
                    onBubblePop = { navController.navigate(BubblePop) },
                    onFeedMonster = { navController.navigate(FeedMonster) },
                    onTracing = { navController.navigate(Tracing) }
                )
            }
            composable<BubblePop> {
                BubblePopScreen(
                    onFinished = { correct, total ->
                        navController.navigate(RoundComplete(correct, total)) {
                            popUpTo<BubblePop> { inclusive = true }
                        }
                    }
                )
            }
            composable<FeedMonster> {
                FeedMonsterScreen(
                    onFinished = { correct, total ->
                        navController.navigate(RoundComplete(correct, total)) {
                            popUpTo<FeedMonster> { inclusive = true }
                        }
                    }
                )
            }
            composable<Tracing> {
                TracingScreen(
                    onFinished = { correct, total ->
                        navController.navigate(RoundComplete(correct, total)) {
                            popUpTo<Tracing> { inclusive = true }
                        }
                    }
                )
            }
            composable<RoundComplete> { entry ->
                val args = entry.toRoute<RoundComplete>()
                RoundCompleteScreen(
                    correct = args.correct,
                    total = args.total,
                    onDone = { navController.popBackStack<Landing>(inclusive = false) }
                )
            }
            composable<Stats> {
                StatsScreen(onLock = {
                    isParentUnlocked = false
                    navController.popBackStack<Landing>(inclusive = false)
                })
            }
            composable<Settings> {
                SettingsScreen(onLock = {
                    isParentUnlocked = false
                    navController.popBackStack<Landing>(inclusive = false)
                })
            }
        }
    }
}
