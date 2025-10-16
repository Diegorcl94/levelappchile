package cl.duoc.levelappchile.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelappchile.data.model.UsedGame
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.data.repository.RepositoryImpl
import kotlinx.coroutines.launch

class SellViewModel: ViewModel() {
    private val repo = RepositoryImpl(FirebaseService())

    fun publishUsed(title: String, platform: String, price: Double, city: String, photo: Uri?,
                    onDone: ()->Unit, onError:(String)->Unit) =
        viewModelScope.launch {
            try {
                val uid = repo.currentUid() ?: throw Exception("No auth")
                val url = photo?.let { repo.uploadImage("used/${uid}_${System.currentTimeMillis()}.jpg", it) } ?: ""
                repo.postUsedGame(UsedGame(title=title, platform=platform, price=price,
                    sellerUid = uid, city = city, photoUrl = url))
                onDone()
            } catch (e: Exception) { onError(e.message ?: "Error publicando") }
        }
}