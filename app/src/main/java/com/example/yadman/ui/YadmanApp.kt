package com.example.yadman.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yadman.NotesViewModel

private data class TabItem(val route: String, val title: String, val icon: ImageVector)

@Composable
fun YadmanApp(vm: NotesViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val tabs = listOf(
        TabItem("notes", "یادداشت‌ها", Icons.Filled.Home),
        TabItem("pinned", "نشان‌شده‌ها", Icons.Filled.Star),
        TabItem("settings", "تنظیمات", Icons.Filled.Settings)
    )
    val showBottomBar = tabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo("notes") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.title) },
                            label = { Text(tab.title) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == "notes") {
                FloatingActionButton(onClick = { navController.navigate("editor/-1") }) {
                    Icon(Icons.Filled.Add, contentDescription = "یادداشت جدید")
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "notes",
            modifier = Modifier.padding(padding)
        ) {
            composable("notes") {
                NotesScreen(vm, onlyPinned = false, onOpenNote = { id ->
                    navController.navigate("editor/$id")
                })
            }
            composable("pinned") {
                NotesScreen(vm, onlyPinned = true, onOpenNote = { id ->
                    navController.navigate("editor/$id")
                })
            }
            composable("settings") {
                SettingsScreen(vm)
            }
            composable(
                route = "editor/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("noteId") ?: -1L
                EditorScreen(vm, id, onBack = { navController.popBackStack() })
            }
        }
    }
}
