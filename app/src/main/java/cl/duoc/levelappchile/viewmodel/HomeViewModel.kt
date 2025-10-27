package cl.duoc.levelappchile.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelappchile.data.model.Banner
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repo = RepositoryImpl(FirebaseService())

    private val _banners = MutableStateFlow<List<Banner>>(emptyList())
    val banners: StateFlow<List<Banner>> = _banners

    init {
        loadBanners()
    }

    /** 🔹 Carga los banners de Firebase al iniciar la vista */
    private fun loadBanners() {
        viewModelScope.launch {
            try {
                val list = repo.getBanners()
                _banners.value = list
                Log.d("HomeViewModel", "✅ Banners cargados: ${list.size}")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "❌ Error cargando banners: ${e.message}")
                _banners.value = emptyList()
            }
        }
    }
}
