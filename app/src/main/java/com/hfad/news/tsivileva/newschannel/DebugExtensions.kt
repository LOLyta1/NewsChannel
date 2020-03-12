package com.hfad.news.tsivileva.newschannel

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hfad.news.tsivileva.newschannel.model.local.DescriptionAndContent
import com.hfad.news.tsivileva.newschannel.model.local.DescriptionAndFav
import com.hfad.news.tsivileva.newschannel.model.local.Description

val DEBUG_LOG = "mylog"
val CACHE_LOG = "cache_log"
val FEED_VIEW_MODEL_LOG = "feed_view_model_log"
val REMOTE_LOG = "remote_log"

fun printNewsDescriptionList(list: List<Description>) {
    list.forEach {
        Log.d(DEBUG_LOG, " id=${it.id};" +
                "\ntitle=${it.title.substring(10)}" +
                "\n date=${it.date}" +
                "\n link=${it.link}" +
                "\n picture=${it.pictureSrc}" +
                "\n sourceKind=${it.sourceKind}"+
                "\n================================"
        )
    }
}

fun printNewsAndContentList(list: MutableList<DescriptionAndContent?>) {
    list.forEach {
        val description = it?.description
        Log.d(DEBUG_LOG, "================================")
        Log.d(DEBUG_LOG, " id=${description?.id};" +
                "\ntitle=${getTenSymbols(description?.title)}" +
                "\n date=${description?.date}" +
                "\n link=${description?.link}" +
                "\n picture=${description?.pictureSrc}" +
                "\n sourceKind=${description?.sourceKind}")
        val info = it?.content
        Log.d(DEBUG_LOG, "\nid=${info?.id};" +
                "\nnewsId=${info?.descriptionId}" +
                "\n content=${getTenSymbols(info?.contentText)}")

    }
}

 fun printNewsAndFav(descriptionAndFav: List<DescriptionAndFav?>?) {
     descriptionAndFav?.forEach {
         val description = it?.description
         Log.d(DEBUG_LOG, "================================")
         Log.d(DEBUG_LOG, " id=${description?.id};" +
                 "\ntitle=${getTenSymbols(description?.title)}" +
                 "\n date=${description?.date}" +
                 "\n link=${description?.link}" +
                 "\n picture=${description?.pictureSrc}" +
                 "\n sourceKind=${description?.sourceKind}")
         val info = it?.favorite
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
                        cached: MutableLiveData<MutableList<Description>>
) {
    Log.d(DEBUG_LOG, "$className.$methodName Данные по кэшу ")
    Log.d(DEBUG_LOG, "$className.$methodName Содержимое кэша: ")

    cached.value?.forEach {
        Log.d(DEBUG_LOG, "$className.$methodName --- $it")
    }
}


fun printCachedMutableList(className: String,
                           methodName: String,
                           cached: MutableList<Description>
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

fun printId(list: MutableList<Description>) {
    list.forEach {
        Log.d(DEBUG_LOG, "в списке ID-${it.id}")
    }
}