package cl.duoc.levelappchile.data.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val coverUrl: String = "",
    val totalSpent: Double = 0.0
) {
    fun rank(): Pair<String, Int> {
        // Puedes ajustar la lógica de ranking si quieres
        return when {
            totalSpent >= 3_000_000 -> "Leyenda" to 40
            totalSpent >= 1_200_000 -> "Aventurero" to 30
            else -> "Novato" to 10
        }
    }
}