package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository

class FeedDetailsViewModel : ViewModel() {
    private val repository = RemoteRepository.NewsContent()
    val news = repository.content
    val isDownloadSuccessful = repository.isDownloadSuccessful

    fun loadHabrContent(url: String) {
        repository.downloadHabr(url)
    }

    fun loadProgerContent(url: String) {
        repository.downloadProger(url)
    }

    fun stopLoad() {
        repository.unsubscribeHabr()
        repository.unsubscribeProger()

    }

    override fun onCleared() {
        super.onCleared()
        stopLoad()
    }

    fun refreshData(){
      news.postValue(NewsItem())
      isDownloadSuccessful.postValue(true)
    }
}