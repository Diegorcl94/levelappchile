package cl.duoc.levelappchile.data.model

data class UsedGame(
    val id: String = "",
    val title: String = "",
    val platform: String = "",
    val price: Double = 0.0,
    val sellerUid: String = "",
    val photoUrl: String = "",
    val city: String = "",
    val createdAt: Long = System.currentTimeMillis()
)