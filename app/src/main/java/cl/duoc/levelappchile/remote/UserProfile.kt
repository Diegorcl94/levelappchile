package cl.duoc.levelappchile.data.remote

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val coverUrl: String = "",
    val totalSpent: Double = 0.0
)