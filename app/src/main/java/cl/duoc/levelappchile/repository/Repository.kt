package cl.duoc.levelappchile.domain.repository

import android.net.Uri
import cl.duoc.levelappchile.data.model.NewsItem
import cl.duoc.levelappchile.data.model.Product
import cl.duoc.levelappchile.data.model.UsedGame
import cl.duoc.levelappchile.data.model.UserProfile
import cl.duoc.levelappchile.data.model.*

interface Repository {
    /* AUTH */
    suspend fun register(email: String, pass: String)
    suspend fun login(email: String, pass: String)
    suspend fun reset(email: String)
    fun currentUid(): String?

    /* PERFIL */
    suspend fun getProfile(uid: String): UserProfile?
    suspend fun saveProfile(profile: UserProfile)
    suspend fun updateProfileField(uid: String, field: String, value: String)

    // CATÁLOGO / BANNERS / NEWS
    suspend fun getNewProducts(): List<Product>
    suspend fun getProductsByCategory(cat: String): List<Product>
    suspend fun getBanners(): List<Banner>
    suspend fun getNews(): List<NewsItem>   // si ya n

    /* COMPRAS / USADOS */
    suspend fun getPurchases(uid: String): List<Product>
    suspend fun postUsedGame(game: UsedGame)
    suspend fun getUsedByUser(uid: String): List<UsedGame>

    /* STORAGE */
    suspend fun uploadImage(path: String, uri: Uri): String
}