package com.hfad.news.tsivileva.newschannel.repository.local

import androidx.room.*
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ILocaApi {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news:NewsItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: List<NewsItem>)

    @Update
    fun update(news:NewsItem)

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME ORDER BY  $DATABASE_DATE_COLUMN  DESC")
    fun selectAll() : List<NewsItem>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE $DATABASE_FAVOURITE_COLUMN=='true' ")
    fun selectFavorite() : List<NewsItem>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE ${DATABASE_LINK_COLUMN} LIKE :link ")
    fun selectByLink(link:String) : NewsItem

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE ${DATABASE_TITLE_COLUMN} LIKE :title ")
    fun selectByTitle(title:String) : Single<List<NewsItem>>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE ${DATABASE_ID_COLUMN}=:id ")
    fun selectById(id: Long) : NewsItem

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE ${DATABASE_ID_COLUMN}=:id ")
    fun selectByIdObserc(id:Long) : Single<NewsItem>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME ORDER BY  $DATABASE_DATE_COLUMN ASC ")
    fun selectAllSortedByDateAsc() : Observable<List<NewsItem>>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME ORDER BY $DATABASE_DATE_COLUMN DESC ")
    fun selectAllSortedByDateDesc() : Observable<List<NewsItem>>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE $DATABASE_SOURCE_COLUMN=:source ORDER BY  $DATABASE_DATE_COLUMN   ASC")
    fun selectSortedByDateAsc(source: FeedsSource) : Observable<List<NewsItem>>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE $DATABASE_SOURCE_COLUMN =:source ORDER BY  $DATABASE_DATE_COLUMN  DESC")
    fun selectSortedByDateDesc(source: FeedsSource) : Observable<List<NewsItem>>

    @Query(value = " UPDATE $DATABASE_TABLE_NAME SET $DATABASE_CONTENT_COLUMN =:content WHERE $DATABASE_ID_COLUMN =:id")
    fun updateById(content: String, id: Long)

    @Query(value = "SELECT $DATABASE_ID_COLUMN  FROM  $DATABASE_TABLE_NAME WHERE $DATABASE_LINK_COLUMN LIKE :link")
    fun selectIdByLink(link:String): Long

    @Transaction
    fun update(content: String, id: Long) :NewsItem{
        updateById(content,id)
       return selectById(id)
    }

    @Transaction
    fun addToFavorite(link: String){
        val id=selectIdByLink(link)
        val news=selectById(id)
        news.isFavorite=true
        insert(news)
    }

}