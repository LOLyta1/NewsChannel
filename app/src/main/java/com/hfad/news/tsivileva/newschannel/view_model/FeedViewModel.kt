package com.hfad.news.tsivileva.newschannel.view_model

import android.util.Log
import androidx.lifecycle.ViewModel

import com.hfad.news.tsivileva.newschannel.repository.remote.*

class FeedViewModel : ViewModel() {
    private val repository = RemoteRepository.AllNews()
    val news = repository.news
    val subscription = repository.subscription
    val isDownloadSuccessful = repository.isDownloadSuccessful

    fun loadAllNews() {
        repository.load()
        Log.d("mylog", "NewsViewModel. loadAllNews()")
    }


    fun stopDownload() {
        subscription.value?.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        stopDownload()
        news.value?.clear()
    }
}