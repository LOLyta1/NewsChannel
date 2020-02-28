package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.FeedsContentSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.getIdInLink
import com.hfad.news.tsivileva.newschannel.repository.remote.FeedContentRepository

class FeedContentViewModel : ViewModel() {
    private val repository = FeedContentRepository()

    val isDownloadSuccessful = repository.isDownloadSuccessful
    val newsList = repository.newsCache
    val newsItem = repository.newsItem

    fun loadContent(url: String, source: FeedsContentSource) {
        val _newsItem = newsList.find { it.link == url || it.id == getIdInLink(url) }
        if (_newsItem != null){
            when(source){
                FeedsContentSource.HABR -> {}
                FeedsContentSource.PROGER -> {}
            }
        }else{
              
        }



    }


    fun stopLoad() {
        repository.stopLoad()
    }

    override fun onCleared() {
        super.onCleared()
        stopLoad()
    }

    fun refreshData() {
        newsItem.postValue(NewsItem())
        isDownloadSuccessful.postValue(true)
    }
}