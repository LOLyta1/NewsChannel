package com.hfad.news.tsivileva.newschannel.adapter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class Sources {
    HABR,
    PROGER
}

data class NewsItem(
        var picture: String? = null,
        var id: Long? = null,
        var sourceKind: Sources? = null,
        var link: String? = null,
        var date: String? = null,
        var title: String? = null,
        var content: String? = null
)
