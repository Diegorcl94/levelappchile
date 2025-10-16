package cl.duoc.levelappchile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.levelappchile.data.model.Product
import coil.compose.rememberAsyncImagePainter

@Composable
fun GameCard(item: Product, onClick:()->Unit) {
    Card(
        modifier = Modifier
            .width(180.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.imageUrl),
            contentDescription = item.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        Column(Modifier.padding(12.dp)) {
            Text(item.title, maxLines = 1)
            Spacer(Modifier.height(4.dp))
            Text("$${"%,.0f".format(item.price)}", color = MaterialTheme.colorScheme.primary)
        }
    }
}