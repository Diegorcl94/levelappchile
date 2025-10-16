package cl.duoc.levelappchile.data.model

data class Review(
    val id: String = "",
    val productId: String = "",
    val uid: String = "",
    val text: String = "",
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)