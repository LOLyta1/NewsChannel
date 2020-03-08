package com.hfad.news.tsivileva.newschannel

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hfad.news.tsivileva.newschannel.model.local.NewsAndContent
import com.hfad.news.tsivileva.newschannel.model.local.NewsAndFav
import com.hfad.news.tsivileva.newschannel.model.local.NewsDescription

val DEBUG_LOG = "mylog"
val CACHE_LOG = "cache_log"
val FEED_VIEW_MODEL_LOG = "feed_view_model_log"
val REMOTE_LOG = "remote_log"

fun printNewsDescriptionList(list: List<NewsDescription>) {
    list.forEach {
        Log.d(DEBUG_LOG, " id=${it.id};" +
                "\ntitle=${it.title.substring(10)}" +
                "\n date=${it.date}" +
                "\n link=${it.link}" +
                "\n picture=${it.picture}" +
                "\n sourceKind=${it.sourceKind}"+
                "\n================================"
        )
    }
}

fun printNewsAndContentList(list: MutableList<NewsAndContent?>) {
    list.forEach {
        val description = it?.newsInfo
        Log.d(DEBUG_LOG, "================================")
        Log.d(DEBUG_LOG, " id=${description?.id};" +
                "\ntitle=${getTenSymbols(description?.title)}" +
                "\n date=${description?.date}" +
                "\n link=${description?.link}" +
                "\n picture=${description?.picture}" +
                "\n sourceKind=${description?.sourceKind}")
        val info = it?.newsContent
        Log.d(DEBUG_LOG, "\nid=${info?.id};" +
                "\nnewsId=${info?.newsId}" +
                "\n content=${getTenSymbols(info?.content)}")

    }
}

 fun printNewsAndFav(newsAndFav: List<NewsAndFav?>?) {
     newsAndFav?.forEach {
         val description = it?.newsInfo
         Log.d(DEBUG_LOG, "================================")
         Log.d(DEBUG_LOG, " id=${description?.id};" +
                 "\ntitle=${getTenSymbols(description?.title)}" +
                 "\n date=${description?.date}" +
                 "\n link=${description?.link}" +
                 "\n picture=${description?.picture}" +
                 "\n sourceKind=${description?.sourceKind}")
         val info = it?.newsFav
         Log.d(DEBUG_LOG, "\nid=${info?.id};" +
                 "\nnewsId=${info?.newsId}" +
                 "\nis fav=${info?.isFav}")

     }
}
fun getTenSymbols(string: String?): String {
    if (string != null && string.length > 10) {
        return string.substring(0, 9)
    } else
        return "<10 символов ($string)"
}

fun printCachedLiveData(className: String,
                        methodName: String,
                        cached: MutableLiveData<MutableList<NewsDescription>>
) {
    Log.d(DEBUG_LOG, "$className.$methodName Данные по кэшу ")
    Log.d(DEBUG_LOG, "$className.$methodName Содержимое кэша: ")

    cached.value?.forEach {
        Log.d(DEBUG_LOG, "$className.$methodName --- $it")
    }
}


fun printCachedMutableList(className: String,
                           methodName: String,
                           cached: MutableList<NewsDescription>
) {
    Log.d(DEBUG_LOG, "$className.$methodName Содержимое списка  ")
    cached.forEach {
        Log.d(CACHE_LOG, "$className.$methodName --- $it")
    }
    Log.d(CACHE_LOG, "_________________________________________________________________________________")

}

fun logStateSwiper(swiper: SwipeRefreshLayout?, info: String) {
    Log.d(DEBUG_LOG, info)
    when (swiper?.isRefreshing) {
        true -> Log.d(DEBUG_LOG, "swiper is REFRESH")
        false -> Log.d(DEBUG_LOG, "swiper is NOT REFRESH")
    }
}

fun printFragmentsInManager(manager: FragmentManager) {
    manager.fragments.forEach {
        Log.d(DEBUG_LOG, "${it.tag}")
    }
}

fun logIt(className: String?, methodName: String?, info: String, tag: String? = DEBUG_LOG) {
    //  val date=Date(GregorianCalendar().gregorianChange.time)
    Log.d(tag, "$className.$methodName(): $info")
}

fun printId(list: MutableList<NewsDescription>) {
    list.forEach {
        Log.d(DEBUG_LOG, "в списке ID-${it.id}")
    }
}