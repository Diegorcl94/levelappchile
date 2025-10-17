package cl.duoc.levelappchile.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import cl.duoc.levelappchile.R

@Composable
fun LoginBackgroundLottie() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.login_loop)
    )
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.fillMaxSize()
    )
}