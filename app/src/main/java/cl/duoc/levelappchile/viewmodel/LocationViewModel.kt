package cl.duoc.levelappchile.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelappchile.core.util.isInChile
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(app: Application): AndroidViewModel(app) {
    private val _inChile = MutableStateFlow<Boolean?>(null)
    val inChile = _inChile.asStateFlow()

    @SuppressLint("MissingPermission")
    fun check() {
        val client = LocationServices.getFusedLocationProviderClient(getApplication<Application>())
        viewModelScope.launch {
            try {
                val loc = client.lastLocation.await()
                _inChile.value = loc?.let { isInChile(it.latitude, it.longitude) } ?: true
            } catch (_: Exception) { _inChile.value = true } // tolerante si falla
        }
    }
}