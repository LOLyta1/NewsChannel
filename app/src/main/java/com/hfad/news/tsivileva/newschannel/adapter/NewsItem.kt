package com.hfad.news.tsivileva.newschannel.adapter

import android.util.Log
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import java.text.SimpleDateFormat
import java.util.*

enum class Source(val link:String) {
    HABR("https://habr.com/ru/rss/all/"),
    PROGER("https://tproger.ru/feed/")
}

data class NewsItem(
        var picture: String? = null,
        var id: Long? = null,
        var sourceKind: Source? = null,
        var link: String ?= "",
        var reserveLink:String?=null,
        var date: Date? = null,
        var title: String? = null,
        var content: String? = null
)
{
    fun getStringDate():String{
        date?.let {
            Log.d(DEBUG_LOG,"getStringDate() of ${date?.time}")
            return SimpleDateFormat("dd MMM yyyy, hh:mm",Locale.US).format(date?.time)
        }
      return ""
    }
}
