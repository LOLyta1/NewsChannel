package com.hfad.news.tsivileva.newschannel.model.local

import android.os.Parcelable
import androidx.room.*
import com.hfad.news.tsivileva.newschannel.FeedsSource
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
@Entity(tableName = "Description")
data class NewsDescription(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id_desc")
        var id: Long=0L,
        var date: Date? = Date(0L),
        var picture: String = "",
        var sourceKind: FeedsSource = FeedsSource.BOTH,
        var link: String = "",
        var title: String = ""

) : Parcelable {
    fun dateToString(): String? {
        return SimpleDateFormat("dd MMM yyyy, hh:mm:ss a Z", Locale.getDefault()).format(date?.time)
    }
}

@Parcelize
@Entity(tableName = "Favorite",
        indices = [Index(value = ["fav_id_desc"], unique = true)])
data class Favorite(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id_fav")
        var id: Long?,

        @ColumnInfo(name = "fav_id_desc")
        @ForeignKey(entity = NewsDescription::class,
                childColumns = ["id_desc"],
                parentColumns = ["fav_id_desc"],
                onDelete = ForeignKey.CASCADE)
        var newsId: Long,

        var isFav: Boolean
) : Parcelable


@Entity(tableName = "Content",
        indices = [Index(value = ["id_desc"], unique = true)])
data class NewsContent(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id_content")
        var id: Long?,

        @ColumnInfo(name = "id_desc")
        @ForeignKey(entity = NewsDescription::class,
                childColumns = ["id_desc"],
                parentColumns = ["id_desc"],
                onDelete = ForeignKey.CASCADE)
        var newsId: Long?,

        @ColumnInfo(name = "content")
        var content: String
)

data class NewsAndContent(
        @Embedded
        var newsInfo: NewsDescription,

        @Relation(entity = NewsContent::class,
                parentColumn = "id_desc",
                entityColumn = "id_desc")
        var newsContent: NewsContent
)

data class NewsAndFav(
        @Embedded
        var newsInfo: NewsDescription,

        @Embedded
        var newsFav: Favorite?
)

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
