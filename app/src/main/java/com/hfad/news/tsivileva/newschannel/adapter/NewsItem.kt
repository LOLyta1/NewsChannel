package com.hfad.news.tsivileva.newschannel.adapter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class Sources{
    HABR,
    Proger
}
data class NewsItem(
        var id: Long? = null,
        var date: String? = null,
        var title: String? = null,
        var content: String? = null,
        var picture: String? = null,
        var link: String? = null,
        var sourceKind : Sources?=null
)
