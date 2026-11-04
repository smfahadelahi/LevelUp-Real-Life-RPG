package com.novara.levelup.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val Scheme = darkColorScheme(primary=Color(0xFF9B7BFF), secondary=Color(0xFFFFC857), tertiary=Color(0xFF55D6D2), background=Color(0xFF0B1020), surface=Color(0xFF151C30))
@Composable fun LevelUpTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme=Scheme, content=content) }
