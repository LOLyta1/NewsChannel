package com.hfad.news.tsivileva.newschannel.view_model

import android.util.Log
import androidx.lifecycle.ViewModel

import com.hfad.news.tsivileva.newschannel.repository.remote.*

class FeedViewModel : ViewModel() {
    private val repository = RemoteRepository.AllNews()
    val news = repository.news
    val isDownloadSuccessful = repository.isDownloadSuccessful

    fun loadAllNews() {
       repository.load()
        Log.d("mylog","NewsViewModel. loadAllNews()")
    }

    fun stopLoad(){
        repository.stopLoad()
        Log.d("mylog","NewsViewModel. stopLoad()()")

    }

    override fun onCleared() {
       super.onCleared()
       stopLoad()
        news.value?.clear()
        Log.d("mylog","NewsViewModel. onCleared()")
    }
}