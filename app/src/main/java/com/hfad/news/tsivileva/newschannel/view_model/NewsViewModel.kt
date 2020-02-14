package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem

import com.hfad.news.tsivileva.newschannel.repository.remote.*

class NewsViewModel : ViewModel() {
    private val repository = RemoteRepository().AllNews()

    val newsListLiveData = repository.newsLiveData
    val loadSuccessfulLiveData = repository.loadSuccessfulLiveData


    fun loadAllNews() {
       repository.load()
    }

    fun stopLoad(){
        repository.stopLoad()
    }


}