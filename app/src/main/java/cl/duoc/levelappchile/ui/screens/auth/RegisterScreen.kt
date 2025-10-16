package cl.duoc.levelappchile.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.levelappchile.ui.viewmodel.AuthState
import cl.duoc.levelappchile.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(nav: NavController, vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Success) nav.navigate("home"){ popUpTo("register"){inclusive=true} }
    }

    Column(Modifier.padding(24.dp)) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(name, {name=it}, label={Text("Nombre")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(email, {email=it}, label={Text("Email")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(pass, {pass=it}, label={Text("Contraseña")},
            visualTransformation = PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        Button(onClick = { vm.register(name,email,pass) }, modifier=Modifier.fillMaxWidth()) { Text("Registrarme") }
        if (state is AuthState.Loading) CircularProgressIndicator()
        if (state is AuthState.Error) Text((state as AuthState.Error).msg, color=MaterialTheme.colorScheme.error)
    }
}