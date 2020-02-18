package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository

class NewsContentViewModel : ViewModel() {
    private val repository = RemoteRepository.NewsContent()

    private val newContentLiveData = repository.contentLiveData
    fun getNewsContentLiveData() : LiveData<NewsItem> = newContentLiveData

    private val loadingNewsStatus = repository.loadingSuccessful
    fun getLoadingNewsStatus() : LiveData<Boolean> = loadingNewsStatus

    private val cachedList = repository.cachedList
    fun getCachedList() : List<NewsItem> = cachedList

    fun setNewsContent(item: NewsItem){
        repository.contentLiveData.postValue(item)
    }

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

    override fun onCleared() {
        super.onCleared()
        stopLoad()
    }
}