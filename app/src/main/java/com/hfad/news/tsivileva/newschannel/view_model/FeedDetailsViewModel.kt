package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteFeedsDetails
import io.reactivex.disposables.Disposable

class FeedDetailsViewModel : ViewModel() {
  private val repository = RemoteFeedsDetails()

    val isDownloadSuccessful = repository.isDownloadSuccessful
    val contentItem=repository.contentItem

     var subscription=repository.disposable

    fun loadContent(url: String,source:FeedsContentSource) {
        repository.downloadFeedsDetails(url, source)
        logIt("FeedDetailsViewModel", "loadContent", "загузка из сети ")
    }

    fun stopLoad() {
      repository.stopLoad()
    }

    override fun onCleared() {
        super.onCleared()
        stopLoad()
    }

    fun refreshData(){
      repository.contentItem.postValue(NewsItem())
      repository.isDownloadSuccessful.postValue(true)
    }
}