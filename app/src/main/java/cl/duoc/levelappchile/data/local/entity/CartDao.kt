package cl.duoc.levelappchile.data.local

import androidx.room.*
import cl.duoc.levelappchile.data.local.entity.CartItem
import cl.duoc.levelappchile.data.local.entity.MyGame
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    // --- Carrito ---
    @Query("SELECT * FROM cart") fun getAll(): Flow<List<CartItem>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: CartItem)
    @Query("DELETE FROM cart WHERE productId = :id") suspend fun remove(id: String)
    @Query("DELETE FROM cart") suspend fun clear()

    // --- Mis Juegos ---
    @Query("SELECT * FROM mygames") fun getMyGames(): Flow<List<MyGame>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun addMyGame(game: MyGame)
}