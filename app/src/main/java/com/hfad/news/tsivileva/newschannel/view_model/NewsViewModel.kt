package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem

import com.hfad.news.tsivileva.newschannel.repository.remote.*

class NewsViewModel : ViewModel() {
    private val repository = RemoteRepository().AllNews()

    var newsListLiveData = repository.newsLiveData
    var loadSuccessfulLiveData = repository.loadSuccessfulLiveData

    fun loadAllNews() {
       repository.load()
    }



/*    fun loadHabrNewsDetails(item: NewsItem?) {
        var observer = object : SingleObserver<HabrContent> {
            override fun onError(e: Throwable) {
                Log.d("my_log", "loadHabrNewsInfo() - onError : ${e.message}")
                e.printStackTrace()
            }

            override fun onSuccess(t: HabrContent) {
                val index = newsArray.indexOf(item)
                item?.content = t.content
                updateHabrNews(index, item)
            }

            override fun onSubscribe(d: Disposable) {
            }
        }
        item?.link?.let {
            RemoteRepository().createObservableHabrItem(it).subscribeWith(observer)
        }
    }*/


}