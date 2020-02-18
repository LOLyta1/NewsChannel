package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.ViewModel

import com.hfad.news.tsivileva.newschannel.repository.remote.*

class NewsViewModel : ViewModel() {
    private val repository = RemoteRepository.AllNews()

    private val newsList = repository.newsLiveData
    fun getNewsList()=newsList

    private val loadStatusLiveData = repository.loadSuccessfulLiveData
    fun getLoadStatusLiveData() = loadStatusLiveData

    fun loadAllNews() {
       repository.load()
    }

    fun stopLoad(){
        repository.stopLoad()
    }

    override fun onCleared() {
       super.onCleared()
       stopLoad()

    }
}