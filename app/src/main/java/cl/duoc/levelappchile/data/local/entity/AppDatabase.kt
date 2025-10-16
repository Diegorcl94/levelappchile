package cl.duoc.levelappchile.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.levelappchile.data.local.entity.CartItem
import android.content.Context

@Database(entities = [CartItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(ctx, AppDatabase::class.java, "levelup.db").build()
                    .also { INSTANCE = it }
            }
    }
}