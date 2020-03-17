package com.hfad.news.tsivileva.newschannel.repository.local

import androidx.room.*
import com.hfad.news.tsivileva.newschannel.model.local.*
import io.reactivex.Single


@Dao
interface ILocaApi {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoDescription(description: Description): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoDescription(descriptions: List<Description>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContent(content: Content): Long

    @Query(value = "SELECT * FROM Description ORDER BY date DESC")
    fun selectAllDescriptions(): List<DescriptionAndFav>

    @Query(value = "SELECT * FROM Description LEFT JOIN  Favorite ON Description.id_desc=Favorite.fav_id_desc WHERE Description.title LIKE :title ")
    fun selectDescriptionByTitle(title: String): Single<List<DescriptionAndFav>>

    @Query("SELECT content FROM Content WHERE id_desc=:id")
    fun selectContentByDescriptionId(id: Long): String

    /*выбор контента и избранного - при фильтрации только избранного (INNER JOIN)*/
    @Query("SELECT * FROM Description  JOIN  Favorite ON Description.id_desc=Favorite.fav_id_desc WHERE Favorite.isFav==1")
    fun selectDescriptionAndFaw():  List<DescriptionAndFav>

    @Query("SELECT * FROM Description  LEFT JOIN  Favorite ON Description.id_desc=Favorite.fav_id_desc")
    fun selectAllDescriptionAndFav():  List<DescriptionAndFav>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoFav(fav: Favorite): Single<Long>

}
