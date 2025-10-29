package cl.duoc.levelappchile.ui.screens.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.levelappchile.data.local.AppDatabase
import cl.duoc.levelappchile.data.local.entity.CartItem
import cl.duoc.levelappchile.ui.components.GameCard
import cl.duoc.levelappchile.ui.viewmodel.CatalogViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    nav: NavController,
    vm: CatalogViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val dao = remember { AppDatabase.get(ctx).cartDao() }
    val scope = rememberCoroutineScope()

    val cats = listOf("PC", "PS5", "PS4", "GIFT", "SOFTWARE")
    var selected by remember { mutableStateOf(cats.first()) }
    val items by vm.items.collectAsState()
    var cartCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        dao.getAll().collect { cartCount = it.sumOf { i -> i.qty } }
    }

    LaunchedEffect(selected) { vm.load(selected) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo") },
                actions = {
                    IconButton(onClick = { nav.navigate("cart") }) {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge { Text(cartCount.toString()) }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                cats.forEach { c ->
                    FilterChip(
                        selected = selected == c,
                        onClick = { selected = c },
                        label = { Text(c) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) { p ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        GameCard(p) { nav.navigate("detail/${p.id}") }

                        Button(
                            onClick = {
                                scope.launch {
                                    dao.upsert(
                                        CartItem(
                                            productId = p.id,
                                            title = p.title,
                                            price = p.price,
                                            qty = 1
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.width(140.dp)
                        ) { Text("Comprar") }
                    }
                }
            }
        }
    }
}
