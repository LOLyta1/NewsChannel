package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.FEED_VIEW_MODEL_LOG
import com.hfad.news.tsivileva.newschannel.logIt

import com.hfad.news.tsivileva.newschannel.repository.remote.*

class FeedViewModel : ViewModel() {
    private val repository = RemoteRepository.AllNews()
    val news = repository.news
    val subscription = repository.subscription
    val isDownloadSuccessful = repository.isDownloadSuccessful
    val cache=repository.cacheList

    fun loadAllNews() {
        repository.load()
        logIt("FeedViewModel","loadAllNews","", FEED_VIEW_MODEL_LOG)
    }

    fun cleareCache(){
        logIt("FeedViewModel","cleareCache","", FEED_VIEW_MODEL_LOG)
        repository.cleareCache()
    }

    fun stopDownload() {
        logIt("FeedViewModel","stopDownload","", FEED_VIEW_MODEL_LOG)
        subscription.value?.dispose()
    }

    override fun onCleared() {
        logIt("FeedViewModel","onCleared","", FEED_VIEW_MODEL_LOG)
        super.onCleared()
        stopDownload()
    }
}