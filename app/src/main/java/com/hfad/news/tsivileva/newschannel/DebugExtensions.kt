package com.hfad.news.tsivileva.newschannel

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent

val DEBUG_LOG = "mylog"

fun printCachedLiveData(className: String,
                        methodName: String,
                        cached: MutableLiveData<MutableList<NewsItem>>
) {
    Log.d(DEBUG_LOG, "$className.$methodName Данные по кэшу ")
    Log.d(DEBUG_LOG, "$className.$methodName Содержимое кэша: ")

    cached.value?.forEach {
        Log.d(DEBUG_LOG, "$className.$methodName --- $it")
    }}


fun printCachedMutableList(className: String,
                        methodName: String,
                        cached:MutableList<NewsItem>
) {
    Log.d(DEBUG_LOG, "$className.$methodName Данные по списку кэшу ")
    Log.d(DEBUG_LOG, "$className.$methodName Содержимое списка кэша: ")

    cached.forEach {
        Log.d(DEBUG_LOG, "$className.$methodName --- $it")
    }
}

fun logStateSwiper(swiper:SwipeRefreshLayout?,info:String){
      Log.d(DEBUG_LOG, info)
    when(swiper?.isRefreshing){
        true->Log.d(DEBUG_LOG,"swiper is REFRESH")
        false->Log.d(DEBUG_LOG,"swiper is NOT REFRESH")
    }
}

fun printFragmentsInManager(manager :FragmentManager){
     manager.fragments.forEach {
         Log.d(DEBUG_LOG,"${it.tag}")
     }
}

fun printLiveDataObservers(liveData : LiveData<Any>){
 }
