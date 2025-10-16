package cl.duoc.levelappchile.core.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val darkScheme = darkColorScheme(
    primary = XboxGreen,
    onPrimary = Color.White,
    secondary = XboxGreenDark,
    background = Night,
    surface = Graphite,
    onSurface = TextOnDark
)

@Composable
fun LevelUpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkScheme,
        typography = Typography(),
        content = content
    )
}