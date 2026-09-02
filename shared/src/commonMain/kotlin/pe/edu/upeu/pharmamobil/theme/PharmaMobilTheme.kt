package pe.edu.upeu.pharmamobil.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF00897B),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF4CAF93),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF7FAF9),
    onBackground = Color(0xFF1B1F1E),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1F1E),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4DB6AC),
    onPrimary = Color(0xFF00382E),
    secondary = Color(0xFF80CBB5),
    onSecondary = Color(0xFF00382E),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE1E3E1),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE1E3E1),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun PharmaMobilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}