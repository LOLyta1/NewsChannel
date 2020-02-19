package com.hfad.news.tsivileva.newschannel.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository

class FeedDetailsViewModel : ViewModel() {
    val repository = RemoteRepository.NewsContent()
    val cachedNewsItemLiveData = repository.cachedNewsItem
    val loadeddNewsItemLiveData = repository.loadedNewsItem

    val loadingNewsStatus = repository.loadingSuccessful
    val cachedList = repository.cachedList

    fun setNewsContent(item: NewsItem) {
        repository.cachedNewsItem.postValue(item)
        Log.d("mylog", "NewsContentViewModel. setNewsContent()")
    }

    fun loadHabrContent(url: String) {
        repository.loadHabr(url)
        Log.d("mylog", "NewsContentViewModel. loadHabrContent()")
    }

    fun loadProgerContent(url: String) {
        repository.loadProger(url)
        Log.d("mylog", "NewsContentViewModel. loadProgerContent()")

    }

    fun stopLoad() {
        Log.d("mylog", "NewsContentViewModel. stopLoad()")
        repository.stopLoadHabr()
        repository.stopLoadProger()
    }

    override fun onCleared() {
        Log.d("mylog", "NewsContentViewModel. onCleared()")
        super.onCleared()
        stopLoad()
    }
}