package cl.duoc.levelappchile.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.*
import cl.duoc.levelappchile.R

@Composable
fun LoginBackgroundLottie() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.login_loop) // pon tu JSON en res/raw
    )
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop  // ocupa toda la pantalla
    )
}