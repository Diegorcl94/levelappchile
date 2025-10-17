package cl.duoc.levelappchile.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.ui.viewmodel.AuthState
import cl.duoc.levelappchile.ui.viewmodel.AuthViewModel
import cl.duoc.levelappchile.ui.components.LoginBackgroundLottie
import cl.duoc.levelappchile.R

@Composable
fun LoginScreen(
    nav: NavController,
    vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            nav.navigate("home") { popUpTo("login") { inclusive = true } }
        }
    }

    Box(Modifier.fillMaxSize()) {
        // 1) Fondo animado Lottie (no se toca)
        LoginBackgroundLottie()

        // 2) Scrim para legibilidad sobre el fondo
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.25f),
                            Color.Black.copy(alpha = 0.55f)
                        )
                    )
                )
        )

        // 3) Contenido del formulario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Logo en lugar del texto
            Image(
                painter = painterResource(id = R.drawable.levelup_logo),
                contentDescription = "Logo LevelUp",
                modifier = Modifier
                    .width(300.dp)      // ajusta el tamaño según tu logo
                    .height(160.dp)
                    .padding(bottom = 12.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { vm.login(email, pass) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state !is AuthState.Loading
            ) {
                Text("Ingresar")
            }

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { nav.navigate("reset") }) {
                Text("¿Olvidaste tu contraseña?")
            }
            TextButton(onClick = { nav.navigate("register") }) {
                Text("Crear cuenta")
            }

            Spacer(Modifier.height(12.dp))
            when (state) {
                is AuthState.Loading -> CircularProgressIndicator()
                is AuthState.Error -> Text(
                    (state as AuthState.Error).msg,
                    color = MaterialTheme.colorScheme.error
                )
                else -> Unit
            }
        }
    }
}