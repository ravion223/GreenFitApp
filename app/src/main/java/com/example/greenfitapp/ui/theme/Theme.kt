package com.example.greenfitapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val GreenFitBackground = Color(0xFF121212)
val GreenFitPrimary = Color(0xFF2ECC71)
private val DarkColorScheme = darkColorScheme(
    primary = GreenFitPrimary,
    background = GreenFitBackground,
    surface = GreenFitBackground,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = GreenFitPrimary,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun GreenFitAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // dynamicColor залишаємо false, щоб бачити саме ТВОЇ кольори
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}