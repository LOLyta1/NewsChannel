package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable

class NewsContentViewModel : ViewModel() {
    private val repository = RemoteRepository().NewsContent()

    val newContentLiveData = repository.newsContentLiveData
    val loadingNewsSuccessful = repository.loadingSuccessful
    val cached=repository.cachedList


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


}