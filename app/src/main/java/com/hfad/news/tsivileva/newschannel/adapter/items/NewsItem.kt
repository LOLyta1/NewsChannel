package com.hfad.news.tsivileva.newschannel.adapter.items

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class NewsItem() : Parcelable {
     var date: String? = null
     var title: String? = null
     var summarry: String? = null
     var picture: String? = null
     var link: String? = null
}