package cl.duoc.levelappchile.ui.screens.profile

import cl.duoc.levelappchile.ui.components.RemoteCoverImage
import cl.duoc.levelappchile.ui.components.RemoteCircleImage
import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.data.model.Product
import cl.duoc.levelappchile.data.model.UsedGame
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.data.repository.RepositoryImpl
import cl.duoc.levelappchile.ui.viewmodel.ProfileViewModel
import cl.duoc.levelappchile.ui.viewmodel.SellViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import cl.duoc.levelappchile.data.local.AppDatabase
import cl.duoc.levelappchile.data.local.entity.MyGame

/* ===== Helpers ===== */

@Composable
private fun RemoteCoverImage(url: String?, modifier: Modifier = Modifier) {
    if (url.isNullOrBlank()) {
        Box(
            modifier
                .background(
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
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}

@Composable
private fun RemoteCircleImage(url: String?, modifier: Modifier = Modifier) {
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
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}

@Composable
private fun UrlEditDialog(
    title: String,
    current: String?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember(current) { mutableStateOf(current.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Pega aquí la URL (https)") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Tip: usa una imagen pública (Imgur, GitHub raw, Unsplash). Debe comenzar con https://",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text.trim()) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

/* ===== Pantalla ===== */

@Composable
fun ProfileScreen(
    nav: NavController,
    vm: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    sellVm: SellViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val profile by vm.profile.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    val repo = remember { RepositoryImpl(FirebaseService()) }
    val scope = rememberCoroutineScope()
    var uid by remember { mutableStateOf<String?>(null) }
    var posts by remember { mutableStateOf<List<UsedGame>>(emptyList()) }
    var purchases by remember { mutableStateOf<List<Product>>(emptyList()) }

    // 🔹 Juegos comprados desde Room
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val dao = remember { AppDatabase.get(ctx).cartDao() }
    var myGames by remember { mutableStateOf(listOf<MyGame>()) }

    LaunchedEffect(Unit) {
        dao.getMyGames().collect { myGames = it }
    }

    var showEditAvatar by remember { mutableStateOf(false) }
    var showEditCover by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    fun refreshAll() {
        scope.launch {
            uid = repo.currentUid()
            uid?.let {
                posts = repo.getUsedByUser(it)
                purchases = repo.getPurchases(it)
            }
        }
    }
    LaunchedEffect(Unit) { refreshAll() }

    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp))
        ) {
            RemoteCoverImage(
                url = profile?.coverUrl,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { showEditCover = true },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.Image,
                    contentDescription = "Cambiar portada (URL)",
                    tint = Color.White
                )
            }

            Row(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(80.dp)) {
                    RemoteCircleImage(
                        url = profile?.photoUrl,
                        modifier = Modifier.size(80.dp)
                    )
                    IconButton(
                        onClick = { showEditAvatar = true },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Cambiar foto (URL)",
                            tint = Color.White
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = profile?.name ?: "Gamer",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = profile?.email ?: "Aplicación móvil",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFB9C6D3)
                    )
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Stat("Amigos", "0"); Stat("Siguiendo", "0"); Stat("Seguidores", "0")
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) { Text("Aparece desconectado") }

        if (errorMsg != null) {
            Text(
                errorMsg!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        val tabs = listOf("Publicaciones", "Mis juegos", "Vender")
        TabRow(selectedTabIndex = selectedTab, modifier = Modifier.padding(top = 8.dp)) {
            tabs.forEachIndexed { i, t ->
                Tab(
                    selected = selectedTab == i,
                    onClick = { selectedTab = i },
                    text = { Text(t) }
                )
            }
        }

        when (selectedTab) {
            0 -> PublicationsList(posts)
            1 -> LocalPurchasesList(myGames)
            2 -> SellTab(onPublished = { selectedTab = 0; refreshAll() }, sellVm = sellVm)
        }
    }

    val currentUid = uid
    if (showEditAvatar && currentUid != null) {
        UrlEditDialog(
            title = "Cambiar foto de perfil (URL)",
            current = profile?.photoUrl,
            onDismiss = { showEditAvatar = false },
            onConfirm = { newUrl ->
                scope.launch {
                    try {
                        repo.updateProfileField(currentUid, "photoUrl", newUrl)
                        showEditAvatar = false
                    } catch (e: Exception) {
                        errorMsg = e.message ?: "No se pudo actualizar la foto"
                    }
                }
            }
        )
    }
    if (showEditCover && currentUid != null) {
        UrlEditDialog(
            title = "Cambiar portada (URL)",
            current = profile?.coverUrl,
            onDismiss = { showEditCover = false },
            onConfirm = { newUrl ->
                scope.launch {
                    try {
                        repo.updateProfileField(currentUid, "coverUrl", newUrl)
                        showEditCover = false
                    } catch (e: Exception) {
                        errorMsg = e.message ?: "No se pudo actualizar la portada"
                    }
                }
            }
        )
    }
}

/* ----------------- Componentes auxiliares ----------------- */

@Composable
private fun Stat(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun PublicationsList(list: List<UsedGame>) {
    if (list.isEmpty()) {
        SectionText("Aún no has publicado juegos."); return
    }
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(list) { g ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = g.photoUrl,
                        contentDescription = g.title,
                        modifier = Modifier.size(72.dp).clip(CircleShape),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(g.title, style = MaterialTheme.typography.titleMedium)
                        Text((g.platform ?: "-") + " • " + (g.city ?: "-"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$${"%,.0f".format(g.price)}", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun LocalPurchasesList(list: List<MyGame>) {
    if (list.isEmpty()) {
        SectionText("Aún no tienes compras registradas."); return
    }
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(list) { p ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = p.imageUrl,
                        contentDescription = p.title,
                        modifier = Modifier.size(72.dp).clip(CircleShape),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(p.title, style = MaterialTheme.typography.titleMedium)
                        Text("Precio: $${"%,.0f".format(p.price)}", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionText(msg: String) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text(msg, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SellTab(onPublished: () -> Unit, sellVm: cl.duoc.levelappchile.ui.viewmodel.SellViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var title by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("PS5") }
    var price by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Santiago") }
    var desc by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var sending by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // ✅ Permisos de cámara/galería
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.CAMERA] == true ||
                perms[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        if (!granted) {
            Toast.makeText(context, "Se requieren permisos para usar la cámara o galería", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        if (bmp != null) {
            @Suppress("DEPRECATION")
            val path = android.provider.MediaStore.Images.Media.insertImage(
                context.contentResolver, bmp, "used_${System.currentTimeMillis()}", null
            )
            photoUri = Uri.parse(path)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) photoUri = uri
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Publicar juego físico", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(title, { title = it }, label = { Text("Nombre del juego") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(platform, { platform = it }, label = { Text("Plataforma (PS5/PS4/PC)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(price, { price = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(city, { city = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(desc, { desc = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    )
                }
                cameraLauncher.launch(null)
            }) { Text("Tomar foto") }

            OutlinedButton(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    )
                }
                galleryLauncher.launch("image/*")
            }) { Text("Elegir de galería") }

            if (photoUri != null) AssistChip(onClick = { photoUri = null }, label = { Text("Quitar foto") })
        }

        if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)

        Button(
            onClick = {
                sending = true; error = null
                val p = price.toDoubleOrNull() ?: 0.0
                sellVm.publishUsed(
                    title = title, platform = platform, price = p,
                    city = city, photo = photoUri,
                    onDone = {
                        sending = false
                        title = ""; platform = "PS5"; price = ""; city = "Santiago"; desc = ""; photoUri = null
                        onPublished()
                    },
                    onError = { msg -> sending = false; error = msg }
                )
            },
            modifier = Modifier.fillMaxWidth(), enabled = !sending
        ) {
            if (sending) CircularProgressIndicator(modifier = Modifier.size(18.dp))
            else Text("Publicar")
        }
    }
}