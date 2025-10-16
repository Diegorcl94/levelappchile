package cl.duoc.levelappchile.data.model

data class Product(
    val id: String = "",
    val title: String = "",
    val platform: String = "", // PC, PS5, PS4, GIFT, SOFTWARE
    val price: Double = 0.0,
    val imageUrl: String = "",
    val isNew: Boolean = false,
    val tags: List<String> = emptyList(),
    val description: String = ""
)