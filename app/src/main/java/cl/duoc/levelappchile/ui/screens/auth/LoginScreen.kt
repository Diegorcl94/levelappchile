package cl.duoc.levelappchile.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.ui.viewmodel.AuthState
import cl.duoc.levelappchile.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(nav: NavController, vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Success) nav.navigate("home"){ popUpTo("login"){inclusive=true} }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("LevelUp Store", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = email, onValueChange = {email=it}, label={Text("Email")})
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = pass, onValueChange = {pass=it}, label={Text("Contraseña")},
                visualTransformation = PasswordVisualTransformation())
            Spacer(Modifier.height(12.dp))
            Button(onClick = { vm.login(email, pass) }, modifier = Modifier.fillMaxWidth()) { Text("Ingresar") }
            TextButton(onClick = { nav.navigate("reset") }) { Text("¿Olvidaste tu contraseña?") }
            TextButton(onClick = { nav.navigate("register") }) { Text("Crear cuenta") }
            if (state is AuthState.Loading) { CircularProgressIndicator() }
            if (state is AuthState.Error) Text((state as AuthState.Error).msg, color=MaterialTheme.colorScheme.error)
        }
    }
}