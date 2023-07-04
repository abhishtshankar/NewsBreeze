package com.abhisht28.newsfeed

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedArticle::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedArticleDao(): SavedArticleDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "news_feed_database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}

