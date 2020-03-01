package com.hfad.news.tsivileva.newschannel.adapter

import com.hfad.news.tsivileva.newschannel.FeedsSource
import java.text.SimpleDateFormat
import java.util.*


data class NewsItem(
        var picture: String = "",
        var id: Long = 0L,
        var sourceKind: FeedsSource = FeedsSource.BOTH,
        var link: String = "",
        var date: Date? = Date(0L),
        var title: String = "",
        var content: String = ""
) {
    fun dateToString(): String {
        return SimpleDateFormat("dd MMM yyyy, hh:mm", Locale.US).format(date?.time)
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
