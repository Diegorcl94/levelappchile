package cl.duoc.levelappchile.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(nav: NavController, vm: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val profile by vm.profile.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        profile?.let { p ->
            val (rank, disc) = p.rank()
            Text("Nombre: ${p.name}")
            Text("Email: ${p.email}")
            Text("Total gastado: $${"%,.0f".format(p.totalSpent)}")
            Text("Rango: $rank  •  Descuento: $disc%")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { nav.navigate("sell") }) { Text("Vender juego usado") }
        } ?: Text("Cargando perfil...")
    }
}