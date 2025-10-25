package cl.duoc.levelappchile.data.remote

import cl.duoc.levelappchile.data.model.Banner
import android.net.Uri
import cl.duoc.levelappchile.data.model.NewsItem
import cl.duoc.levelappchile.data.model.Product
import cl.duoc.levelappchile.data.model.UsedGame
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseService {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    /* ============== AUTH ============== */
    suspend fun register(email: String, pass: String) =
        auth.createUserWithEmailAndPassword(email, pass).awaitUnit()

    suspend fun login(email: String, pass: String) =
        auth.signInWithEmailAndPassword(email, pass).awaitUnit()

    fun currentUid(): String? = auth.currentUser?.uid
    suspend fun reset(email: String) = auth.sendPasswordResetEmail(email).awaitUnit()

    /* ============== PERFIL (modelo remoto) ============== */
    suspend fun getProfile(uid: String): UserProfile? =
        db.collection("profiles").document(uid).get().await().toObject(UserProfile::class.java)

    suspend fun saveProfile(profile: UserProfile) {
        db.collection("profiles").document(profile.uid).set(profile).awaitUnit()
    }

    suspend fun updateProfileField(uid: String, field: String, value: String) {
        db.collection("profiles").document(uid).update(mapOf(field to value)).awaitUnit()
    }

    /* ============== PRODUCTS / CATALOGO ============== */
    suspend fun getNewProducts(): List<Product> =
        db.collection("products")
            .whereEqualTo("isNew", true)
            .get()
            .await()
            .toObjects(Product::class.java)

    suspend fun getProductsByCategory(cat: String): List<Product> =
        db.collection("products")
            .whereEqualTo("platform", cat)
            .get()
            .await()
            .toObjects(Product::class.java)

    /* ============== NEWS (carrusel) ============== */
    suspend fun getNews(): List<NewsItem> =
        db.collection("news")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .await()
            .toObjects(NewsItem::class.java)

    /* ============== COMPRAS / USADOS ============== */
    suspend fun getPurchases(uid: String): List<Product> =
        db.collection("purchases").document(uid).collection("items")
            .get().await().toObjects(Product::class.java)

    suspend fun postUsedGame(game: UsedGame) {
        db.collection("usedGames").add(game).awaitUnit()
    }

    suspend fun getBanners(): List<Banner> =
        db.collection("banners")
            .whereEqualTo("isActive", true)
            .orderBy("order", Query.Direction.ASCENDING)
            .get()
            .await()
            .toObjects(Banner::class.java)

    suspend fun getUsedByUser(uid: String): List<UsedGame> =
        db.collection("usedGames")
            .whereEqualTo("sellerUid", uid)
            .get()
            .await()
            .toObjects(UsedGame::class.java)

    /* ============== STORAGE ============== */
    suspend fun uploadImage(path: String, uri: Uri): String {
        val ref = storage.reference.child(path)
        ref.putFile(uri).awaitUnit()
        return ref.downloadUrl.await().toString()
    }
}

/* ====== Helpers await ====== */
suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            if (it.isSuccessful) cont.resume(it.result)
            else cont.resumeWithException(it.exception ?: Exception("Firebase error"))
        }
    }

suspend fun com.google.android.gms.tasks.Task<*>.awaitUnit() =
    suspendCancellableCoroutine<Unit> { cont ->
        addOnCompleteListener {
            if (it.isSuccessful) cont.resume(Unit)
            else cont.resumeWithException(it.exception ?: Exception("Firebase error"))
        }
    }
