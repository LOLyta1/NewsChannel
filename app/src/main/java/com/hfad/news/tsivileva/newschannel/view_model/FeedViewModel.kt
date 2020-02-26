package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.FEED_VIEW_MODEL_LOG
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.logIt

import com.hfad.news.tsivileva.newschannel.repository.remote.*
import io.reactivex.disposables.CompositeDisposable

class FeedViewModel : ViewModel() {
    private val repository = RemoteFeeds()

    var newsStore =repository.newsStore
    val isDownloadSuccessful = repository.isDownloadSuccessful
    //TODO заменить на CompositeDisposable
    var subscriptions =CompositeDisposable()

    fun loadFeeds(url: String) {
        val _subscriprion=repository.downloadFeeds(url)
        _subscriprion?.let{ subscriptions.add(it)}
        logIt("FeedViewModel","loadHabrFeeds","", FEED_VIEW_MODEL_LOG)
    }


    fun cleareCache(){
        repository.cleareCache()
        logIt("FeedViewModel","cleareCache","", FEED_VIEW_MODEL_LOG)
    }

    fun stopDownload() {
        subscriptions?.dispose()
        logIt("FeedViewModel","stopDownload","", FEED_VIEW_MODEL_LOG)
    }

    fun sort(sortKind: Sort){
     repository.sortNews(sortKind)
    }

    override fun onCleared() {
        logIt("FeedViewModel","onCleared","", FEED_VIEW_MODEL_LOG)
        super.onCleared()
        stopDownload()
    }


}