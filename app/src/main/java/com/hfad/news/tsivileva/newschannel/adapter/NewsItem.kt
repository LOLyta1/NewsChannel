package com.hfad.news.tsivileva.newschannel.adapter

import com.hfad.news.tsivileva.newschannel.FeedsSource
import java.text.SimpleDateFormat
import java.util.*


data class NewsItem(
        var picture: String? = null,
        var id: Long? = null,
        var sourceKind: FeedsSource? = null,
        var link: String? = "",
        var reserveLink: String? = null,
        var date: Date? = null,
        var title: String? = null,
        var content: String? = null
) {
    fun dateToString(): String {
        date?.let {
            return SimpleDateFormat("dd MMM yyyy, hh:mm", Locale.US).format(date?.time)
        }
        return ""
    }

    fun isEmpty() = (
            picture == null
                    && sourceKind == null
                    && link == null
                    && date == null
                    && title == null
                    && content == null
            )

}
