package cl.duoc.levelappchile.data.model

data class Banner(
    val imageUrl: String = "",
    val title: String = "",     // opcional (por si después lo usas)
    val order: Int = 0,
    val isActive: Boolean = true,
    val route: String = ""      // ej: "detail/ps5-elden-ring" o "catalog"
)