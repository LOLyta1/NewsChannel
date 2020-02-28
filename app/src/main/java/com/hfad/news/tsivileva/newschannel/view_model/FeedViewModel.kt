package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.FEED_VIEW_MODEL_LOG
import com.hfad.news.tsivileva.newschannel.FeedsContentSource
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.logIt

import com.hfad.news.tsivileva.newschannel.repository.remote.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class FeedViewModel : ViewModel() {
    private val repository = RemoteFeeds()
    var newsStore =repository.newsStore
    var cache=repository.cache
    val isDownloadSuccessful = repository.isDownloadSuccessful
    lateinit var subscriptions:Disposable

    fun downloadFeeds(source: FeedsSource) {
        logIt("FeedViewModel","loadFeeds","", FEED_VIEW_MODEL_LOG)
        if(cache.isEmpty()){
                subscriptions=repository.downloadFeeds(source)
                logIt("FeedViewModel","loadHabrFeeds","ЗАГРУЗКА С ИНТЕРНЕТА", FEED_VIEW_MODEL_LOG)
            }else{
              newsStore.postValue(cache)
            logIt("FeedViewModel","loadHabrFeeds","ЗАГРУЗКА ИЗ КЭША", FEED_VIEW_MODEL_LOG)
            isDownloadSuccessful.postValue(true)
            }
        }


    fun cleareCache(){
      cache= mutableListOf()
        logIt("FeedViewModel","cleareCache","", FEED_VIEW_MODEL_LOG)
    }

    fun stopDownload() {
        subscriptions.dispose()
        logIt("FeedViewModel","stopDownload","", FEED_VIEW_MODEL_LOG)
    }

    override fun onCleared() {
        logIt("FeedViewModel","onCleared","", FEED_VIEW_MODEL_LOG)
        super.onCleared()
        stopDownload()
    }


}