package com.hfad.news.tsivileva.newschannel.adapter

import com.hfad.news.tsivileva.newschannel.FeedsSource
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*


data class NewsItem(
        var date: Date? = Date(0L),
        var picture: String = "",
        var id: Long = 0L,
        var sourceKind: FeedsSource = FeedsSource.BOTH,
        var link: String = "",
        var title: String = "",
        var content: String = ""
) {
    fun dateToString(): String{
        return  SimpleDateFormat("dd MMM yyyy, hh:mm:ss a Z", Locale.getDefault()).format(date?.time)
       // return SimpleDateFormat("dd MMM yyyy, hh:mm:ss", Locale.US).format(date?.time)
    }

    fun isEmpty() = (
            picture == ""
                    && id == 0L
                    && link == ""
                    && date == Date(0L)
                    && title == ""
                    && content == ""

            )

}
