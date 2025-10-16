package cl.duoc.levelappchile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelappchile.data.model.Product
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val repo = RepositoryImpl(FirebaseService())
    private val _news = MutableStateFlow<List<Product>>(emptyList())
    val news = _news.asStateFlow()

    init {
        viewModelScope.launch {
            _news.value = repo.getNewProducts()
        }
    }
}