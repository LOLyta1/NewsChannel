package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteFeedsDetails
import io.reactivex.disposables.Disposable

class FeedDetailsViewModel : ViewModel() {
  private val repository = RemoteFeedsDetails()

    val newsStore = repository.newsStore
    val isDownloadSuccessful = repository.isDownloadSuccessful
    var subscription:Disposable?=null

    fun loadContent(url: String) {
        subscription=repository.downloadFeedsDetails(url)
    }

    fun stopLoad() {
       subscription?.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        stopLoad()
    }

    fun refreshData(){
      newsStore.postValue(NewsItem())
      isDownloadSuccessful.postValue(true)
    }
}