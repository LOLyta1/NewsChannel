package com.hfad.news.tsivileva.newschannel.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hfad.news.tsivileva.newschannel.*
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ILocaApi {
    //News
    @Query(value = "SELECT * FROM $DATABASE_NEWS_TABLE_NAME ORDER BY  $DATABASE_DATE_COLUMN  DESC")
    fun selectAllNews(): List<NewsDescription>

    @Query(value = "SELECT * FROM $DATABASE_NEWS_TABLE_NAME WHERE ${DATABASE_TITLE_COLUMN} LIKE :title ")
    fun selectNewsByTitle(title: String): Single<List<NewsDescription>>

    @Query(value = "SELECT * FROM $DATABASE_NEWS_TABLE_NAME ORDER BY  $DATABASE_DATE_COLUMN ASC ")
    fun selectAllSortedByDateAsc(): Observable<List<NewsDescription>>

    @Query(value = "SELECT * FROM $DATABASE_NEWS_TABLE_NAME ORDER BY $DATABASE_DATE_COLUMN DESC ")
    fun selectAllSortedByDateDesc(): Observable<List<NewsDescription>>

    @Query(value = "SELECT * FROM $DATABASE_NEWS_TABLE_NAME WHERE $DATABASE_SOURCE_COLUMN=:source ORDER BY  $DATABASE_DATE_COLUMN   ASC")
    fun selectSortedByDateAsc(source: FeedsSource): Observable<List<NewsDescription>>

    @Query(value = "SELECT * FROM $DATABASE_NEWS_TABLE_NAME WHERE $DATABASE_SOURCE_COLUMN =:source ORDER BY  $DATABASE_DATE_COLUMN  DESC")
    fun selectSortedByDateDesc(source: FeedsSource): Observable<List<NewsDescription>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoNews(newsDescription: NewsDescription): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoNews(newsDescriptions: List<NewsDescription>)

    //Content
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContent(content:NewsContent):Long

    @Query("SELECT $DATABASE_CONTENT_COLUMN FROM $DATABASE_CONTENT_TABLE_NAME WHERE $DATABASE_NEWS_ID_COLUMN=:id")
    fun selectContentByNewsId(id: Long) : String

    @Query(value = "SELECT * FROM News WHERE $DATABASE_NEWS_ID_COLUMN=:id")
    fun selectNewsAndContent(id:Long): NewsAndContent?

}
