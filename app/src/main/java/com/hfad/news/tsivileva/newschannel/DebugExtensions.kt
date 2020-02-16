package com.hfad.news.tsivileva.newschannel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem

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