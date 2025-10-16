package cl.duoc.levelappchile.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.ui.viewmodel.HomeViewModel
import coil.compose.AsyncImage
import androidx.compose.animation.animateContentSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(nav: NavController, vm: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val news by vm.news.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Novedades", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        val pagerState = rememberPagerState(pageCount = { maxOf(news.size, 1) })
        HorizontalPager(state = pagerState, modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .animateContentSize()
        ) { page ->
            val item = news.getOrNull(page)
            if (item != null) {
                Card {
                    AsyncImage(model = item.imageUrl, contentDescription = item.title,
                        modifier = Modifier.fillMaxSize())
                }
            } else {
                Card { Box(Modifier.fillMaxSize()) }
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