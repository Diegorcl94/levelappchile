package cl.duoc.levelappchile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey val productId: String,
    val title: String,
    val price: Double,
    val qty: Int
)