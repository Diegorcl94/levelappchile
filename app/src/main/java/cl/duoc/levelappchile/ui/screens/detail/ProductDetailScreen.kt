package cl.duoc.levelappchile.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ProductDetailScreen(id: String, nav: NavController) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Detalle de producto $id", style = MaterialTheme.typography.headlineSmall)
        // Aquí: botón “Agregar al carrito” (Room), reseña con foto (similar a SellUsed)
    }
}