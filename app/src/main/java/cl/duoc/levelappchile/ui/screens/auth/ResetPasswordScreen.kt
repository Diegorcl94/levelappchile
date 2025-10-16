package cl.duoc.levelappchile.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.ui.viewmodel.AuthState
import cl.duoc.levelappchile.ui.viewmodel.AuthViewModel

@Composable
fun ResetPasswordScreen(nav: NavController, vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var email by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    Column(Modifier.padding(24.dp)) {
        Text("Recuperar contraseña", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(email, {email=it}, label={Text("Email")}, modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        Button(onClick = { vm.reset(email) }, modifier=Modifier.fillMaxWidth()) { Text("Enviar correo") }
        if (state is AuthState.Message) Text((state as AuthState.Message).msg, color = MaterialTheme.colorScheme.primary)
        if (state is AuthState.Error) Text((state as AuthState.Error).msg, color=MaterialTheme.colorScheme.error)
    }
}