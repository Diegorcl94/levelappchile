package cl.duoc.levelappchile

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.*
import cl.duoc.levelappchile.core.theme.LevelUpTheme
import cl.duoc.levelappchile.ui.navigation.AppNavHost
import cl.duoc.levelappchile.viewmodel.LocationViewModel

class MainActivity : ComponentActivity() {

    private val locationVm: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                PermissionGate(
                    onGranted = { locationVm.loadLastLocation() }
                ) {
                    // 👇 Aquí ya cargas TODA tu app con navegación
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
private fun PermissionGate(
    onGranted: () -> Unit,
    content: @Composable () -> Unit
) {
    var granted by remember { mutableStateOf(false) }

    val requestPerms = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        granted = (grants[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        if (granted) onGranted()
    }

    LaunchedEffect(Unit) {
        requestPerms.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    if (granted) content() else {
        // Pantalla simple mientras se conceden permisos (opcional)
        Surface { Text("Solicitando permisos de ubicación…") }
    }
}