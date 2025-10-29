package cl.duoc.levelappchile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.levelappchile.data.local.entity.CartItem
import cl.duoc.levelappchile.data.local.entity.MyGame

@Database(entities = [CartItem::class, MyGame::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(ctx, AppDatabase::class.java, "levelapp.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}