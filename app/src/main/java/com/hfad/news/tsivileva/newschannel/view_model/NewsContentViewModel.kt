package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository

class NewsContentViewModel : ViewModel() {
    private val repository = RemoteRepository.NewsContent()

    val newContentLiveData = repository.contentLiveData
    val loadingNewsSuccessful = repository.loadingSuccessful
    val cachedList = repository.cachedList

    fun loadHabrContent(url: String) {
        repository.loadHabr(url)
    }

    fun loadProgerContent(url: String) {
        repository.loadProger(url)
    }

    fun stopLoad() {
        repository.stopLoadHabr()
        repository.stopLoadProger()
    }

    fun cleareNewsContent(){
        repository.cleareContent()
    }
}