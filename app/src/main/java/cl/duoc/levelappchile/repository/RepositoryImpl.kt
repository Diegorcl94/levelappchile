package cl.duoc.levelappchile.data.repository

import android.net.Uri
import cl.duoc.levelappchile.data.model.*
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.domain.repository.Repository

class RepositoryImpl(private val remote: FirebaseService) : Repository {
    override suspend fun register(email: String, pass: String) = remote.register(email, pass)
    override suspend fun login(email: String, pass: String) = remote.login(email, pass)
    override suspend fun reset(email: String) = remote.reset(email)
    override fun currentUid(): String? = remote.currentUid()

    override suspend fun getProfile(uid: String) = remote.getProfile(uid)
    override suspend fun saveProfile(profile: UserProfile) = remote.saveProfile(profile)

    override suspend fun getNewProducts() = remote.getNewProducts()
    override suspend fun getProductsByCategory(cat: String) = remote.getProductsByCategory(cat)
    override suspend fun getNews() = remote.getNews()

    override suspend fun postUsedGame(game: UsedGame) = remote.postUsedGame(game)
    override suspend fun uploadImage(path: String, uri: Uri) = remote.uploadImage(path, uri)
}