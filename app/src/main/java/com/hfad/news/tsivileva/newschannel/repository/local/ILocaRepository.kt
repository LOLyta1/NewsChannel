package com.hfad.news.tsivileva.newschannel.repository.local

import androidx.room.*
import com.hfad.news.tsivileva.newschannel.DATABASE_FAVOURITE_COLUMN
import com.hfad.news.tsivileva.newschannel.DATABASE_TABLE_NAME
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem

@Dao
interface ILocaRepository {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news:NewsItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news:List<NewsItem>)

    @Update
    fun update(news:NewsItem)

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME")
    fun selectAll() : List<NewsItem>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE $DATABASE_FAVOURITE_COLUMN=='true'")
    fun selectFavorite() : List<NewsItem>

}