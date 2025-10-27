package cl.duoc.levelappchile.data.model

data class NewsItem(
    val title: String = "",
    val body: String = "",        // 📰 texto de la noticia
    val imageUrl: String = "",    // 🖼️ URL de la imagen
    val createdAt: Long? = null   // 🕒 timestamp en milisegundos
)
