package com.hfad.news.tsivileva.newschannel.adapter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.TypeConverter
import com.hfad.news.tsivileva.newschannel.*
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

@Entity(tableName = DATABASE_TABLE_NAME, primaryKeys = [DATABASE_ID_COLUMN, DATABASE_SOURCE_COLUMN])
data class NewsItem(
        @ColumnInfo(name = DATABASE_ID_COLUMN)
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
        var title: String = "",

        @ColumnInfo(name = DATABASE_CONTENT_COLUMN)
        var content: String = "",

        @ColumnInfo(name = DATABASE_FAVOURITE_COLUMN)
        var isFavorite: Boolean = false

) {

    fun dateToString(): String {
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
