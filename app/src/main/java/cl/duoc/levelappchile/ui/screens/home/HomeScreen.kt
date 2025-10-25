package cl.duoc.levelappchile.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Novedades", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        val count = maxOf(banners.size, 1)
        val pagerState = rememberPagerState(pageCount = { count })

        LaunchedEffect(banners, pagerState.currentPage) {
            if (banners.size > 1) {
                delay(3_000)
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % banners.size)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        ) { page ->
            val item = banners.getOrNull(page)
            Card(Modifier.fillMaxSize(), shape = MaterialTheme.shapes.large) {
                if (item != null) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        if (banners.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                repeat(banners.size) { i ->
                    val selected = i == pagerState.currentPage
                    Box(
                        Modifier
                            .padding(3.dp)
                            .size(if (selected) 10.dp else 8.dp)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { nav.navigate("catalog") }) { Text("Catálogo") }
            Button(onClick = { nav.navigate("news") }) { Text("Noticias") }
            Button(onClick = { nav.navigate("sell") }) { Text("Vender usado") }
            Button(onClick = { nav.navigate("profile") }) { Text("Perfil") }
        }
    }
}