package com.hfad.news.tsivileva.newschannel.repository.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.hfad.news.tsivileva.newschannel.DATABASE_NAME
import com.hfad.news.tsivileva.newschannel.model.local.*


@Database(entities = [Description::class, Content::class, Favorite::class], version = 1)
@TypeConverters(DateConverter::class, SourceKindConverter::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getApi(): ILocaApi

    companion object {
        private var database: NewsDatabase? = null
        fun instance(application: Application): NewsDatabase? {
            return when (database) {
                null -> {
                    database = Room.databaseBuilder(
                            application.applicationContext,
                            NewsDatabase::class.java,
                            DATABASE_NAME)
                            .build()
                    database
                }
                else -> database
            }
        }

        fun destroyInstance() {
            database = null
        }

    }
}