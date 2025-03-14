package com.sasinduprasad.snapzzle.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SnapzzleTheme(content: @Composable () -> Unit) {

    val isDarkMode = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (isDarkMode) {
            darkColorScheme(
                primary = Color(0xFF091619),
                secondary = Color(0xFF132322),
                tertiary = Color(0xFFFFFFFF),
                surface = Color(0XFF47C394)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF091619),
                secondary = Color(0xFF132322),
                tertiary = Color(0xFFFFFFFF),
                surface = Color(0XFF47C394)
            )
        }
    ) {
        content()
    }
}