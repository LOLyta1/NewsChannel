package com.hfad.news.tsivileva.newschannel.repository.local

import android.os.Parcelable
import androidx.room.*
import com.hfad.news.tsivileva.newschannel.*
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat

import java.util.*

class DateConverter() {
    @TypeConverter
    fun toDate(time: Long): Date? {
        return Date(time)
    }

    @TypeConverter
    fun toLong(date: Date?): Long? {
        return date?.time
    }
}

class SourceKindConverter() {
    @TypeConverter
    fun toNumber(sourceKind: FeedsSource): Int {
        return when (sourceKind) {
            FeedsSource.HABR -> 1
            FeedsSource.PROGER -> 2
            FeedsSource.BOTH -> 0
        }
    }

    @TypeConverter
    fun toSourceKind(number: Int): FeedsSource? {
        return when (number) {
            1 -> FeedsSource.HABR
            2 -> FeedsSource.PROGER
            0 -> FeedsSource.BOTH
            else -> null
        }
    }
}
@Parcelize
@Entity(tableName = DATABASE_NEWS_TABLE_NAME)
data class NewsDescription(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = DATABASE_NEWS_ID_COLUMN)
        var id: Long = 0L,

        @ColumnInfo(name = DATABASE_DATE_COLUMN)
        var date: Date? = Date(0L),

        @ColumnInfo(name = DATABASE_PICTURE_COLUMN)
        var picture: String = "",

        @ColumnInfo(name = DATABASE_SOURCE_COLUMN)
        var sourceKind: FeedsSource = FeedsSource.BOTH,

        @ColumnInfo(name = DATABASE_LINK_COLUMN)
        var link: String = "",

        @ColumnInfo(name = DATABASE_TITLE_COLUMN)
        var title: String = ""

) : Parcelable {

    fun dateToString(): String? {
        return SimpleDateFormat("dd MMM yyyy, hh:mm:ss a Z", Locale.getDefault()).format(date?.time)
    }

    fun isEmpty() = (
            picture == ""
                    && id == 0L
                    && link == ""
                    && date == Date(0L)
                    && title == ""


            )

}

@Parcelize
@Entity(tableName = DATABASE_FAV_TABLE_NAME,
        indices = [Index(value = [DATABASE_NEWS_ID_COLUMN], unique = true)])
data class Favorite(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = DATABASE_ID_FAV_COLUMN)
        var id: Long,

        @ColumnInfo(name = DATABASE_NEWS_ID_COLUMN)
        @ForeignKey(entity = NewsDescription::class, childColumns = [DATABASE_NEWS_ID_COLUMN], parentColumns = [DATABASE_NEWS_ID_COLUMN])
        var newsId: Long,

        @ColumnInfo(name = DATABASE_IS_FAV)
        var isFav: Boolean
) : Parcelable

@Entity(tableName = DATABASE_CONTENT_TABLE_NAME,
        indices = [Index(value = [DATABASE_NEWS_ID_COLUMN], unique = true)])

data class NewsContent(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = DATABASE_ID_CONTENT_COLUMN)
        var id: Long?,

        @ColumnInfo(name = DATABASE_NEWS_ID_COLUMN)
        @ForeignKey(entity = NewsDescription::class, childColumns = [DATABASE_NEWS_ID_COLUMN], parentColumns = [DATABASE_NEWS_ID_COLUMN])
        var newsId: Long?,

        @ColumnInfo(name = DATABASE_CONTENT_COLUMN)
        var content: String
)


data class NewsAndContent(
        @Embedded
       var newsInfo:NewsDescription,

        @Relation(parentColumn = DATABASE_NEWS_ID_COLUMN,entity = NewsContent::class,entityColumn = DATABASE_NEWS_ID_COLUMN)
        var newsContent:NewsContent
)

