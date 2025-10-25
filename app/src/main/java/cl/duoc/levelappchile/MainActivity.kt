package cl.duoc.levelappchile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import cl.duoc.levelappchile.ui.navigation.AppScaffold
import cl.duoc.levelappchile.core.theme.LevelUpTheme   // 👈 importa tu tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 🔹 Aplica tu tema global (fondo oscuro + texto blanco)
            LevelUpTheme {
                Surface {
                    val nav = rememberNavController()
                    AppScaffold(nav)   // ← NavHost + BottomBar (oculta en login/register/reset)
                }
            }
        }
    }
}