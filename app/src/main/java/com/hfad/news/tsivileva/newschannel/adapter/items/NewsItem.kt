package com.hfad.news.tsivileva.newschannel.adapter.items

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class NewsItem(
        var date: String? = null,
        var title: String? = null,
        var content: String? = null,
        var picture: String? = null,
        var link: String? = null
)
