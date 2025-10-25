package cl.duoc.levelappchile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelappchile.data.model.Banner
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class HomeViewModel : ViewModel() {

    private val repo = RepositoryImpl(FirebaseService())

    private val _banners = MutableStateFlow<List<Banner>>(emptyList())
    val banners: StateFlow<List<Banner>> = _banners

    init {
        viewModelScope.launch {
            runCatching { repo.getBanners() }
                .onSuccess { list ->
                    Log.d("HomeVM", "Banners cargados: ${list.size}")
                    _banners.value = list
                }
                .onFailure { e ->
                    Log.e("HomeVM", "Error cargando banners", e)
                    _banners.value = emptyList() // evita crash en UI
                }
        }
    }
}