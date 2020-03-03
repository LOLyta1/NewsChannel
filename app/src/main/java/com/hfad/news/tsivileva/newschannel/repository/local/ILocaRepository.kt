package com.hfad.news.tsivileva.newschannel.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.SourceKindConverter
import com.hfad.news.tsivileva.newschannel.view_model.Sort
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ILocaRepository {
    @Transaction
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
    fun selectByLink(link:String?) : Single<NewsItem>

    @Query(value = "SELECT * FROM $DATABASE_TABLE_NAME WHERE ${DATABASE_ID_COLUMN}=:id ")
    fun selectById(id:Long) : NewsItem

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

    @Query(value = " UPDATE $DATABASE_TABLE_NAME SET $DATABASE_CONTENT_COLUMN =:content WHERE $DATABASE_ID_COLUMN =:id AND sourceKind=:source")
    fun updateById(content: String, id: Long, source: FeedsSource)

    @Transaction
    fun update(content: String, id: Long, source: FeedsSource) :NewsItem {
        updateById(content, id, source)
        return selectById(id)
    }

}