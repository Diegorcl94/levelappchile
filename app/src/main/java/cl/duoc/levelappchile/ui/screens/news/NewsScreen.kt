package cl.duoc.levelappchile.ui.screens.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import cl.duoc.levelappchile.data.model.NewsItem
import cl.duoc.levelappchile.ui.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(vm: NewsViewModel = viewModel()) {
    val news: List<NewsItem> by vm.news.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Noticias") }) }
    ) { padding ->
        if (news.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(news) { item -> NewsCard(item) }
            }
        }
    }
}

@Composable
private fun NewsCard(item: NewsItem) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Column(Modifier.fillMaxWidth()) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxWidth().height(180.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}