package cl.duoc.levelappchile.data.local

import androidx.room.*
import cl.duoc.levelappchile.data.local.entity.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart") fun getAll(): Flow<List<CartItem>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: CartItem)
    @Query("DELETE FROM cart WHERE productId = :id") suspend fun remove(id: String)
    @Query("DELETE FROM cart") suspend fun clear()
}