package cl.duoc.levelappchile.ui.screens.news

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.data.remote.FirebaseService
import kotlinx.coroutines.launch

@Composable
fun NewsScreen(nav: NavController) {
    val scope = rememberCoroutineScope()
    var news by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            news = FirebaseService().getNews()
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Noticias", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        news.forEach {
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text(it["title"].toString(), style = MaterialTheme.typography.titleMedium)
                    Text(it["body"].toString())
                }
            }
        }
    }
}