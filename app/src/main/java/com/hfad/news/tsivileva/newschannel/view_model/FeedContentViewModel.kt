package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.FeedsContentSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.getIdInLink
import com.hfad.news.tsivileva.newschannel.repository.*
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable

class FeedContentViewModel : ViewModel() {

    val downloading = MutableLiveData<DownloadingState>()
    private val newsList = mutableListOf<NewsItem>()
    private var newsItem = NewsItem()
    private var disposable:Disposable?=null

    fun download(url: String, source: FeedsContentSource) {
        val _newsItem = newsList.find { it.link == url || it.id == getIdInLink(url) }
        if (_newsItem == null){
            when(source){
                FeedsContentSource.HABR -> {disposable=RemoteRepository.getHabrContentObservable(url).subscribe(::_onNext,::_onError,::_onComplete)}
                FeedsContentSource.PROGER -> {disposable=RemoteRepository.getProgerContentObservable(url).subscribe(::_onNext,::_onError,::_onComplete)}
            }
        }else{
            downloading.postValue(DownloadedFeed(_newsItem))
        }
  }
 fun _onNext(t: NewsItem) {
        newsItem=t
        newsList.add(t)
        downloading.postValue(DownloadingProgress(""))
    }

    fun _onError(e: Throwable) {
        downloading.postValue(DownloadingError(e))
    }

    fun _onComplete() {
        downloading.postValue(DownloadedFeed(newsItem))
        disposable?.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun refreshData() {
        downloading.postValue(DownloadedFeed(NewsItem()))
    }
}