package cl.duoc.levelappchile.ui.screens.sell

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.ui.viewmodel.SellViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun SellUsedScreen(nav: NavController, vm: SellViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var title by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("PS5") }
    var price by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Santiago") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        // Guardamos en cache y convertimos a Uri
        if (bmp != null) {
            val path = android.provider.MediaStore.Images.Media.insertImage(
                nav.context.contentResolver, bmp, "used_${System.currentTimeMillis()}", null
            )
            photoUri = Uri.parse(path)
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vender juego usado", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(title, {title=it}, label={Text("Título")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(platform, {platform=it}, label={Text("Plataforma (PS5/PS4/PC)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(price, {price=it}, label={Text("Precio")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(city, {city=it}, label={Text("Ciudad")}, modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { takePicture.launch(null) }) { Text("Tomar foto") }
            if (photoUri != null) {
                Image(painter = rememberAsyncImagePainter(photoUri), contentDescription = null,
                    modifier = Modifier.size(64.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            vm.publishUsed(title, platform, price.toDoubleOrNull() ?: 0.0, city, photoUri,
                onDone = { nav.popBackStack() },
                onError = { /* muestra snackbar si quieres */ })
        }, modifier = Modifier.fillMaxWidth()) { Text("Publicar") }
    }
}