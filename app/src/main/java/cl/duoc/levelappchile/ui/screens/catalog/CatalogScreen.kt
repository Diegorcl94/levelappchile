package cl.duoc.levelappchile.ui.screens.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.ui.viewmodel.CatalogViewModel
import cl.duoc.levelappchile.ui.components.GameCard

@Composable
fun CatalogScreen(nav: NavController, vm: CatalogViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val cats = listOf("PC", "PS5", "PS4", "GIFT", "SOFTWARE")
    var selected by remember { mutableStateOf(cats.first()) }
    val items by vm.items.collectAsState()

    LaunchedEffect(selected) { vm.load(selected) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Catálogo", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            cats.forEach { c ->
                FilterChip(
                    selected = selected == c,
                    onClick = { selected = c },
                    label = { Text(c) })
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items) { p -> GameCard(p) { nav.navigate("detail/${p.id}") } }
        }
    }
}