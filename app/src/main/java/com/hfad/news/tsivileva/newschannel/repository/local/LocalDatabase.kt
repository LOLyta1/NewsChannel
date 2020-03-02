package com.hfad.news.tsivileva.newschannel.repository.local

import android.app.Application
import androidx.room.*
import com.hfad.news.tsivileva.newschannel.DATABASE_NAME
import com.hfad.news.tsivileva.newschannel.adapter.DateConverter
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.SourceKindConverter

@Database(entities = [NewsItem::class],version = 1)
@TypeConverters(DateConverter::class, SourceKindConverter::class)
abstract class LocalDatabase: RoomDatabase() {
  abstract fun getLocalRepo():ILocaRepository

    companion object{
        private var database:LocalDatabase?=null

        fun instance(application: Application) : LocalDatabase?{
            if(database==null){
                database= Room.databaseBuilder(application.applicationContext,LocalDatabase::class.java, DATABASE_NAME).build()
                return database
            }else{
                return database
            }
        }

        fun destroyInstance(){
            database=null
        }

    }
}