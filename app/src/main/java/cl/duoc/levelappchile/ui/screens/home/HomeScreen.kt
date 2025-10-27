package cl.duoc.levelappchile.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.levelappchile.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(nav: NavController) {
    val vm: HomeViewModel = viewModel()
    val banners by vm.banners.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Novedades",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(8.dp))

        // --- Control del carrusel ---
        val pageCount = maxOf(banners.size, 1)
        val pagerState = rememberPagerState(pageCount = { pageCount })

        // --- Auto-scroll cada 3 segundos ---
        LaunchedEffect(banners, pagerState.currentPage) {
            if (banners.size > 1) {
                delay(3000)
                val nextPage = (pagerState.currentPage + 1) % banners.size
                pagerState.animateScrollToPage(nextPage)
            }
        }

        // --- Carrusel principal ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f) // 👈 relación típica de banners (ajustable)
        ) { page ->
            val banner = banners.getOrNull(page)
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                if (banner != null) {
                    Box(Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = banner.imageUrl,
                            contentDescription = banner.title,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = ContentScale.Fit // 👈 ahora muestra la imagen completa
                        )
                        // Texto opcional sobre el banner
                        if (banner.title.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = banner.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sin imágenes disponibles")
                    }
                }
            }
        }

        // --- Indicadores (puntos del carrusel) ---
        if (banners.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(banners.size) { index ->
                    val selected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(3.dp)
                            .size(if (selected) 10.dp else 8.dp)
                            .background(
                                color = if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                    )
                }
            }
        }

        // --- Botones de navegación ---
        Spacer(Modifier.height(24.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { nav.navigate("catalog") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Catálogo") }

            Button(
                onClick = { nav.navigate("news") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Noticias") }

            Button(
                onClick = { nav.navigate("sell") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Vender usado") }

            Button(
                onClick = { nav.navigate("profile") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Perfil") }
        }
    }
}