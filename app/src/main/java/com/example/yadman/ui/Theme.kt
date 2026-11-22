package com.example.yadman.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val LightColors = lightColorScheme(
    primary = Color(0xFF4F46E5),
    secondary = Color(0xFF0EA5A4),
    background = Color(0xFFF7F7FB),
    surface = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9A93FF),
    secondary = Color(0xFF5EEAD4),
    background = Color(0xFF121218),
    surface = Color(0xFF1C1C24)
)

// رنگ‌های کارت یادداشت‌ها
val noteColors = listOf(
    Color(0xFFFFF4C2),
    Color(0xFFD6F5E3),
    Color(0xFFD9E8FF),
    Color(0xFFFFDCE0),
    Color(0xFFE9DDFF)
)

@Composable
fun YadmanTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors
    ) {
        // برنامه فارسیه پس همه‌چی راست‌چین
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            content()
        }
    }
}
