package cl.duoc.levelappchile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelappchile.data.model.Product
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel: ViewModel() {
    private val repo = RepositoryImpl(FirebaseService())
    private val _items = MutableStateFlow<List<Product>>(emptyList())
    val items = _items.asStateFlow()

    fun load(cat: String) = viewModelScope.launch {
        _items.value = repo.getProductsByCategory(cat)
    }
}