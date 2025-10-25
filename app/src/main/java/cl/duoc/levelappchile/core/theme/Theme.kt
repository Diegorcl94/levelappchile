package cl.duoc.levelappchile.core.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val darkScheme = darkColorScheme(
    primary = XboxGreen,          // botones, acentos
    onPrimary = Color.White,
    secondary = XboxGreenDark,
    onSecondary = Color.White,
    background = Night,           // fondo principal
    onBackground = TextOnDark,    // texto sobre fondo
    surface = Graphite,           // tarjetas, barras, etc.
    onSurface = TextOnDark,       // texto sobre superficies
    error = Color(0xFFFF5555),
    onError = Color.White
)

@Composable
fun LevelUpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkScheme,
        typography = Typography(),
        content = content
    )
}