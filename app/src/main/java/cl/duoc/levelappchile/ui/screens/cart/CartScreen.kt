package cl.duoc.levelappchile.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import cl.duoc.levelappchile.data.local.AppDatabase
import cl.duoc.levelappchile.data.local.entity.CartItem
import cl.duoc.levelappchile.data.local.entity.MyGame
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.launch
import cl.duoc.levelappchile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(nav: NavController) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val dao = remember { AppDatabase.get(ctx).cartDao() }
    val scope = rememberCoroutineScope()

    var items by remember { mutableStateOf(listOf<CartItem>()) }
    var code by remember { mutableStateOf("") }
    var usedDiscount by remember { mutableStateOf(false) }
    var appliedDiscount by remember { mutableStateOf(0.0) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dao.getAll().collect { items = it }
    }

    val subtotal = items.sumOf { it.price * it.qty }
    val discount = if (usedDiscount) appliedDiscount else 0.0
    val total = subtotal - discount

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Carrito de compras") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Text("Tu carrito está vacío.")
            }
        } else {
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items) { item ->
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                                    Text("$${"%,.0f".format(item.price)}")
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        if (item.qty > 1)
                                            scope.launch { dao.upsert(item.copy(qty = item.qty - 1)) }
                                    }) { Icon(Icons.Default.Remove, contentDescription = "Restar") }

                                    Text("${item.qty}", Modifier.width(24.dp))

                                    IconButton(onClick = {
                                        scope.launch { dao.upsert(item.copy(qty = item.qty + 1)) }
                                    }) { Icon(Icons.Default.Add, contentDescription = "Sumar") }

                                    IconButton(onClick = {
                                        scope.launch { dao.remove(item.productId) }
                                    }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Código de descuento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (!usedDiscount) {
                            if (code.trim().replace(" ", "")
                                    .equals("duocuc", ignoreCase = true)
                            ) {
                                appliedDiscount = subtotal * 0.15
                                usedDiscount = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007E33))
                ) {
                    Text("Aplicar descuento", color = Color.White)
                }

                if (usedDiscount) {
                    Text(
                        "Descuento aplicado: -$${"%,.0f".format(appliedDiscount)} (15%)",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    "Total: $${"%,.0f".format(total)}",
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = {
                        showDialog = true
                        scope.launch {
                            items.forEach {
                                dao.addMyGame(
                                    MyGame(
                                        title = it.title,
                                        imageUrl = "",
                                        price = it.price
                                    )
                                )
                            }
                            dao.clear()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Pagar") }
            }
        }
    }

    // 🟩 Diálogo con animación Lottie al finalizar la compra
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 🎬 Animación local desde /res/raw/comprajuego.json
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.comprajuego)
                    )
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        iterations = 1 // una sola vez
                    )

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.size(180.dp)
                    )

                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Compra realizada, ¡disfruta tus juegos!",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showDialog = false
                            nav.navigate("profile")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007E33))
                    ) {
                        Text("Ir a Mis Juegos", color = Color.White)
                    }
                }
            }
        }
    }
}