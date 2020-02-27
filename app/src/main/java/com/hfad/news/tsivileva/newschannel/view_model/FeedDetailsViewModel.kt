package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteFeedsDetails

class FeedDetailsViewModel : ViewModel() {
  private val repository = RemoteFeedsDetails()

    val isDownloadSuccessful = repository.isDownloadSuccessful
    val newsList=repository.newsCache
    val newsItem=repository.newsItem


    fun loadContent(url: String,source:FeedsContentSource) {
         val _newsItem=newsList.find { it.link==url || it.id== getIdInLink(url)}
        if(_newsItem!=null){
            logIt("FeedDetailsViewModel", "loadContent", "загузка из кэша ")
            this.newsItem.postValue(_newsItem)
            isDownloadSuccessful.postValue(true)
        }else{
            logIt("FeedDetailsViewModel", "loadContent", "загузка из сети ")
            repository.downloadFeedsDetails(url, source)
        }
    }

    fun stopLoad() {
      repository.stopLoad()
    }

    override fun onCleared() {
        super.onCleared()
        stopLoad()
    }

    fun refreshData(){
      newsItem.postValue(NewsItem())
      isDownloadSuccessful.postValue(true)
    }
}