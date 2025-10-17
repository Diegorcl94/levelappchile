package cl.duoc.levelappchile.ui.screens.sell

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import cl.duoc.levelappchile.ui.viewmodel.SellViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun SellUsedScreen(
    nav: NavController,
    vm: SellViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val ctx = LocalContext.current

    var title by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("PS5") }
    var price by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Santiago") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Guarda el bitmap en caché y devuelve un Uri content:// seguro (FileProvider)
    fun saveBitmapToCache(bmp: Bitmap): Uri {
        val file = File(ctx.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", file)
    }

    // Tomar foto (preview). Si quieres full resolución, te dejé la variante en el mensaje anterior.
    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        if (bmp != null) {
            photoUri = saveBitmapToCache(bmp)
        }
    }

    // Pedir permiso de cámara en runtime
    val requestCamera = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) takePicture.launch(null)
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title, onValueChange = { title = it },
            label = { Text("Título") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = platform, onValueChange = { platform = it },
            label = { Text("Plataforma") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = price, onValueChange = { price = it },
            label = { Text("Precio") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = city, onValueChange = { city = it },
            label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                val granted = ContextCompat.checkSelfPermission(
                    ctx, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (granted) takePicture.launch(null)
                else requestCamera.launch(Manifest.permission.CAMERA)
            }) {
                Text("Tomar foto")
            }

            if (photoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                vm.publishUsed(
                    title = title,
                    platform = platform,
                    price = price.toDoubleOrNull() ?: 0.0,
                    city = city,
                    photo = photoUri,              // 👈 NOMBRE CORRECTO DEL PARÁMETRO
                    onDone = { nav.popBackStack() },
                    onError = { /* TODO: mostrar snackbar si quieres */ }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && price.isNotBlank()
        ) { Text("Publicar") }
    }
}