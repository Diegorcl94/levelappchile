package cl.duoc.levelappchile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

/**
 * Banner remoto (URL). Si la URL es nula/vacía, muestra un gradiente.
 */
@Composable
fun RemoteCoverImage(
    url: String?,
    modifier: Modifier = Modifier // ← default limpio (no usa dp)
) {
    if (url.isNullOrBlank()) {
        Box(
            modifier.background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFF0F1020), Color(0xFF182B4F), Color(0xFF0F1020))
                )
            )
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * Avatar circular remoto (URL). Si la URL es nula/vacía, muestra un círculo gris.
 * (El tamaño lo decide quien llama: usa Modifier.size(80.dp) en el caller.)
 */
@Composable
fun RemoteCircleImage(
    url: String?,
    modifier: Modifier = Modifier // ← default limpio (no usa dp)
) {
    if (url.isNullOrBlank()) {
        Box(
            modifier
                .clip(CircleShape)
                .background(Color(0xFF263238))
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = modifier.clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}