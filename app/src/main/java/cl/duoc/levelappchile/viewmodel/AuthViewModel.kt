package cl.duoc.levelappchile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelappchile.core.util.isValidEmail
import cl.duoc.levelappchile.core.util.isValidPassword
import cl.duoc.levelappchile.data.model.UserProfile
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val repo = RepositoryImpl(FirebaseService())

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun login(email: String, pass: String) = viewModelScope.launch {
        if (!isValidEmail(email) || !isValidPassword(pass)) {
            _state.value = AuthState.Error("Revisa email/contraseña")
            return@launch
        }
        try {
            _state.value = AuthState.Loading
            repo.login(email, pass)
            _state.value = AuthState.Success
        } catch (e: Exception) {
            _state.value = AuthState.Error(e.message ?: "Error de login")
        }
    }

    fun register(name: String, email: String, pass: String) = viewModelScope.launch {
        try {
            _state.value = AuthState.Loading
            repo.register(email, pass)
            val uid = repo.currentUid()!!
            repo.saveProfile(UserProfile(uid, name, email))
            _state.value = AuthState.Success
        } catch (e: Exception) {
            _state.value = AuthState.Error(e.message ?: "Error de registro")
        }
    }

    fun reset(email: String) = viewModelScope.launch {
        try {
            _state.value = AuthState.Loading
            repo.reset(email)
            _state.value = AuthState.Message("Correo enviado")
        } catch (e: Exception) {
            _state.value = AuthState.Error(e.message ?: "Error al enviar correo")
        }
    }
}

sealed class AuthState {
    object Idle: AuthState()
    object Loading: AuthState()
    object Success: AuthState()
    data class Error(val msg: String): AuthState()
    data class Message(val msg: String): AuthState()
}