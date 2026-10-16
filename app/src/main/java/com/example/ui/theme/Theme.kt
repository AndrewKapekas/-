package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = TikTokPink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4A101C),
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = TikTokCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF004F4D),
    onSecondaryContainer = Color(0xFF99F7F4),
    tertiary = CoinGold,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF473300),
    onTertiaryContainer = Color(0xFFFFE08A),
    background = TikTokDarkBg,
    onBackground = TextPrimary,
    surface = TikTokCardBg,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF26272E),
    onSurfaceVariant = TextSecondary,
    outline = TikTokCardBorder,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TikTokPink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD9E2),
    onPrimaryContainer = Color(0xFF3F0014),
    secondary = Color(0xFF008985),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF99F7F4),
    onSecondaryContainer = Color(0xFF00201F),
    tertiary = CoinGoldDark,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE08A),
    onTertiaryContainer = Color(0xFF241A00),
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF161823),
    surface = Color.White,
    onSurface = Color(0xFF161823),
    surfaceVariant = Color(0xFFEEEEF0),
    onSurfaceVariant = Color(0xFF73747B),
    outline = Color(0xFFE0E0E0),
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to TikTok's signature immersive dark aesthetic
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

