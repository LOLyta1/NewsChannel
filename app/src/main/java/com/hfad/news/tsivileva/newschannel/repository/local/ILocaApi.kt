package com.hfad.news.tsivileva.newschannel.repository.local

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.model.local.*
import io.reactivex.Single


@Dao
interface ILocaApi {

    //News
    @Query(value = "SELECT * FROM Description ORDER BY date DESC")
    fun selectAllDescriptions(): List<NewsDescription>

    @Query(value = "SELECT * FROM Description ORDER BY  date ASC ")
    fun selectAllSortedByDateAsc(): List<NewsDescription>

    @Query(value = "SELECT * FROM Description ORDER BY date DESC ")
    fun selectDescriptionSortedByDateDesc(): List<NewsDescription>

    @Query(value = "SELECT * FROM Description WHERE sourceKind=:source ORDER BY  date   ASC")
    fun selectSortedByDateAsc(source: FeedsSource): List<NewsDescription>

    @Query(value = "SELECT * FROM Description WHERE sourceKind =:source ORDER BY  date  DESC")
    fun selectDescriptionByDateDesc(source: FeedsSource): List<NewsDescription>

    @Query(value = "SELECT * FROM Description WHERE title LIKE :title ")
    fun selectDescriptionByTitle(title: String): List<NewsDescription>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoDescription(newsDescription: NewsDescription): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoDescription(newsDescriptions: List<NewsDescription>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContent(content: NewsContent): Long

    @Query("SELECT content FROM Content WHERE id_desc=:id")
    fun selectContentByDescriptionId(id: Long): String

    @Query(value = "SELECT * FROM Description WHERE id_desc=:descriprionId")
    fun selectDescriptionAndContent(descriprionId: Long): NewsAndContent?


    @RawQuery
    fun rawQuery(query: SupportSQLiteQuery): Long

    @Query("DELETE FROM Description")
    fun cleareNewsDescription()

    @Query("DELETE FROM Content")
    fun cleareNewsContent()

    @Query("DELETE FROM Favorite")
    fun cleareFav()

    /*выбор контента и избранного - при фильтрации только избранного (INNER JOIN)*/
    @Query("SELECT * FROM Description  JOIN  Favorite ON Description.id_desc=Favorite.fav_id_desc WHERE Favorite.isFav==1")
    fun selectDescriptionAndFaw(): Single<List<NewsAndFav>>

    @Query("SELECT * FROM Description  LEFT JOIN  Favorite ON Description.id_desc=Favorite.fav_id_desc")
    fun selectAllDescriptionAndFav():List<NewsAndFav>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoFav(fav: Favorite): Single<Long>

}
