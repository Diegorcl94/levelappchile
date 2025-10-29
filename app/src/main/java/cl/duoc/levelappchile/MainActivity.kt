package cl.duoc.levelappchile

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import cl.duoc.levelappchile.ui.navigation.AppScaffold
import cl.duoc.levelappchile.core.theme.LevelUpTheme
import android.widget.Toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                Surface {
                    val nav = rememberNavController()
                    val context = LocalContext.current

                    // 🔹 Lista dinámica de permisos según la versión de Android
                    val permissions = remember {
                        mutableListOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ).apply {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                add(Manifest.permission.READ_MEDIA_IMAGES)
                            } else {
                                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                    }

                    // 🔹 Launcher para pedir permisos
                    val permissionLauncher =
                        rememberLauncherForActivityResult(
                            ActivityResultContracts.RequestMultiplePermissions()
                        ) { result ->
                            val denied = result.filterValues { !it }.keys
                            if (denied.isNotEmpty()) {
                                Toast.makeText(
                                    context,
                                    "⚠️ Algunos permisos fueron denegados. Algunas funciones podrían no funcionar.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    // 🔹 Pedir permisos automáticamente al abrir app (solo la primera vez)
                    LaunchedEffect(Unit) {
                        permissionLauncher.launch(permissions.toTypedArray())
                    }

                    AppScaffold(nav)
                }
            }
        }
    }
}