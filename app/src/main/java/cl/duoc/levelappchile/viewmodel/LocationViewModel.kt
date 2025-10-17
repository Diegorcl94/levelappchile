package cl.duoc.levelappchile.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UiLocation(val lat: Double, val lon: Double)

class LocationViewModel(app: Application) : AndroidViewModel(app) {

    private val fused by lazy {
        LocationServices.getFusedLocationProviderClient(getApplication<Application>())
    }

    private val _coords = MutableStateFlow<UiLocation?>(null)
    val coords: StateFlow<UiLocation?> = _coords

    @SuppressLint("MissingPermission")
    fun loadLastLocation() {
        viewModelScope.launch {
            val ctx = getApplication<Application>()
            val fine = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarse = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (fine != PackageManager.PERMISSION_GRANTED && coarse != PackageManager.PERMISSION_GRANTED) {
                return@launch
            }

            try {
                val loc = fused.lastLocation.await()
                loc?.let { safe ->
                    _coords.value = UiLocation(lat = safe.latitude, lon = safe.longitude)
                }
            } catch (_: Exception) { }
        }
    }
}