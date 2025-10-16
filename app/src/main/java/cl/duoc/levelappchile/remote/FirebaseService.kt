package cl.duoc.levelappchile.data.remote

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import cl.duoc.levelappchile.data.model.*

class FirebaseService {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    // AUTH
    suspend fun register(email: String, pass: String) =
        auth.createUserWithEmailAndPassword(email, pass).awaitUnit()

    suspend fun login(email: String, pass: String) =
        auth.signInWithEmailAndPassword(email, pass).awaitUnit()

    fun currentUid(): String? = auth.currentUser?.uid

    suspend fun reset(email: String) = auth.sendPasswordResetEmail(email).awaitUnit()

    // PERFIL
    suspend fun getProfile(uid: String): UserProfile? =
        db.collection("profiles").document(uid).get().await().toObject(UserProfile::class.java)

    suspend fun saveProfile(profile: UserProfile) {
        db.collection("profiles").document(profile.uid).set(profile).awaitUnit()
    }

    // PRODUCTS / NEWS
    suspend fun getNewProducts(): List<Product> =
        db.collection("products").whereEqualTo("isNew", true).get().await().toObjects(Product::class.java)

    suspend fun getProductsByCategory(cat: String): List<Product> =
        db.collection("products").whereEqualTo("platform", cat).get().await().toObjects(Product::class.java)

    suspend fun getNews(): List<Map<String, Any>> =
        db.collection("news").orderBy("createdAt").get().await().documents.map { it.data!! }

    // USED GAMES
    suspend fun postUsedGame(game: UsedGame) {
        db.collection("usedGames").add(game).awaitUnit()
    }

    suspend fun uploadImage(path: String, uri: Uri): String {
        val ref = storage.reference.child(path)
        ref.putFile(uri).awaitUnit()
        return ref.downloadUrl.await().toString()
    }
}

/** Helpers await para Tasks sin traer dependencias extra */
suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    kotlinx.coroutines.suspendCancellableCoroutine { cont ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) cont.resume(task.result, null)
            else cont.cancel(task.exception)
        }
    }

suspend fun com.google.android.gms.tasks.Task<*>.awaitUnit() =
    kotlinx.coroutines.suspendCancellableCoroutine<Unit> { cont ->
        addOnCompleteListener { t ->
            if (t.isSuccessful) cont.resume(Unit, null)
            else cont.cancel(t.exception)
        }
    }