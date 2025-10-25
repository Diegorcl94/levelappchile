package cl.duoc.levelappchile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cl.duoc.levelappchile.R

/**
 * Banner remoto (URL). Si la URL es nula/vacía, muestra un gradiente o un drawable placeholder.
 */
@Composable
fun RemoteCoverImage(
    url: String?,
    modifier: Modifier = Modifier.fillMaxWidth(),
    useGradientFallback: Boolean = true
) {
    if (url.isNullOrBlank()) {
        if (useGradientFallback) {
            androidx.compose.foundation.Box(
                modifier.background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0F1020), Color(0xFF182B4F), Color(0xFF0F1020))
                    )
                )
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_cover_placeholder),
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
    } else {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_cover_placeholder),
            error = painterResource(id = R.drawable.ic_cover_placeholder)
        )
    }
}

/**
 * Avatar circular remoto (URL). Si la URL es nula/vacía, usa un drawable placeholder.
 */
@Composable
fun RemoteCircleImage(
    url: String?,
    modifier: Modifier = Modifier.size(80.dp)
) {
    if (url.isNullOrBlank()) {
        Image(
            painter = painterResource(id = R.drawable.ic_profile_placeholder),
            contentDescription = null,
            modifier = modifier.clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = modifier.clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
            error = painterResource(id = R.drawable.ic_profile_placeholder)
        )
    }
}