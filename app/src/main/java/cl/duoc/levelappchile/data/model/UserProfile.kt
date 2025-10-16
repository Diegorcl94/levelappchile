package cl.duoc.levelappchile.data.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val totalSpent: Double = 0.0
) {
    fun rank(): Pair<String, Int> {
        // Simple por gasto total, ajústalo si quieres
        return when {
            totalSpent >= 3000000 -> "Leyenda" to 40
            totalSpent >= 1200000 -> "Aventurero" to 30
            else -> "Novato" to 10
        }
    }
}