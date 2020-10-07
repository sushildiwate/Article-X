package com.sushil.articlex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class ArticleXDataBase : RoomDatabase() {

    abstract fun articleDao(): ArticleXDao

    companion object {
        @Volatile
        private var INSTANCE: ArticleXDataBase? = null

        fun getDatabase(context: Context): ArticleXDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleXDataBase::class.java,
                    "articlex_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}