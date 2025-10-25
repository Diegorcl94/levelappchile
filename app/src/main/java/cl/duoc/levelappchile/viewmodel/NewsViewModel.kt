package cl.duoc.levelappchile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelappchile.data.model.NewsItem
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class NewsViewModel : ViewModel() {
    private val repo = RepositoryImpl(FirebaseService())

    private val _news = MutableStateFlow<List<NewsItem>>(emptyList())
    val news: StateFlow<List<NewsItem>> = _news

    init {
        viewModelScope.launch {
            runCatching { repo.getNews() }
                .onSuccess { list ->
                    Log.d("NewsVM", "News cargadas: ${list.size}")
                    _news.value = list
                }
                .onFailure { e ->
                    Log.e("NewsVM", "Error cargando news", e)
                    _news.value = emptyList()
                }
        }
    }
}