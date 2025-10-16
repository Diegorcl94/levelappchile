package cl.duoc.levelappchile.domain.repository

import android.net.Uri
import cl.duoc.levelappchile.data.model.*

interface Repository {
    suspend fun register(email: String, pass: String)
    suspend fun login(email: String, pass: String)
    suspend fun reset(email: String)
    fun currentUid(): String?

    suspend fun getProfile(uid: String): UserProfile?
    suspend fun saveProfile(profile: UserProfile)

    suspend fun getNewProducts(): List<Product>
    suspend fun getProductsByCategory(cat: String): List<Product>
    suspend fun getNews(): List<Map<String, Any>>

    suspend fun postUsedGame(game: UsedGame)
    suspend fun uploadImage(path: String, uri: Uri): String
}