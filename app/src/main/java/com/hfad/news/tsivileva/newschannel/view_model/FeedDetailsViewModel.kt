package com.hfad.news.tsivileva.newschannel.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository

class FeedDetailsViewModel : ViewModel() {
    val repository = RemoteRepository.NewsContent()
    val cachedNews = repository.content

    val loadingStatus = repository.isDownloadSuccessful
    val cachedList = repository.cachedList


    fun loadHabrContent(url: String) {
        repository.downloadHabr(url)
        Log.d("mylog", "NewsContentViewModel. loadHabrContent()")
    }

    fun loadProgerContent(url: String) {
        repository.downloadProger(url)
        Log.d("mylog", "NewsContentViewModel. loadProgerContent()")

    }

    fun stopLoad() {
        Log.d("mylog", "NewsContentViewModel. stopLoad()")
        repository.stopLoadHabr()
        repository.stopLoadProger()
    }

    override fun onCleared() {
        Log.d("mylog", "NewsContentViewModel. onCleared()")
        super.onCleared()
        stopLoad()
    }
}