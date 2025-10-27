package cl.duoc.levelappchile.data.repository

import android.net.Uri
import cl.duoc.levelappchile.data.model.*
import cl.duoc.levelappchile.data.remote.FirebaseService
import cl.duoc.levelappchile.domain.repository.Repository

// Alias para evitar choque de nombres entre modelos locales y remotos
import cl.duoc.levelappchile.data.model.UserProfile as ModelUserProfile
import cl.duoc.levelappchile.data.remote.UserProfile as RemoteUserProfile

class RepositoryImpl(private val remote: FirebaseService) : Repository {

    /* ============== AUTH ============== */
    override suspend fun register(email: String, pass: String) = remote.register(email, pass)
    override suspend fun login(email: String, pass: String) = remote.login(email, pass)
    override suspend fun reset(email: String) = remote.reset(email)
    override fun currentUid(): String? = remote.currentUid()

    /* ============== PERFIL ============== */
    override suspend fun getProfile(uid: String): ModelUserProfile? =
        remote.getProfile(uid)?.toModel()

    override suspend fun saveProfile(profile: ModelUserProfile) =
        remote.saveProfile(profile.toRemote())

    override suspend fun updateProfileField(uid: String, field: String, value: String) =
        remote.updateProfileField(uid, field, value)

    /* ============== CATÁLOGO / BANNERS / NEWS ============== */
    override suspend fun getNewProducts(): List<Product> = remote.getNewProducts()

    override suspend fun getProductsByCategory(cat: String): List<Product> =
        remote.getProductsByCategory(cat)

    override suspend fun getBanners(): List<Banner> = remote.getBanners()

    override suspend fun getNews(): List<NewsItem> = remote.getNews()

    /* ============== COMPRAS / USADOS ============== */
    override suspend fun getPurchases(uid: String): List<Product> = remote.getPurchases(uid)

    override suspend fun postUsedGame(game: UsedGame) = remote.postUsedGame(game)

    override suspend fun getUsedByUser(uid: String): List<UsedGame> = remote.getUsedByUser(uid)

    /* ============== STORAGE ============== */
    override suspend fun uploadImage(path: String, uri: Uri): String =
        remote.uploadImage(path, uri)
}

/* ============== MAPPERS ============== */
private fun RemoteUserProfile.toModel(): ModelUserProfile =
    ModelUserProfile(
        uid = uid,
        name = name,
        email = email,
        photoUrl = photoUrl,
        coverUrl = coverUrl,
        totalSpent = totalSpent
    )

private fun ModelUserProfile.toRemote(): RemoteUserProfile =
    RemoteUserProfile(
        uid = uid,
        name = name,
        email = email,
        photoUrl = photoUrl,
        coverUrl = coverUrl,
        totalSpent = totalSpent
    )