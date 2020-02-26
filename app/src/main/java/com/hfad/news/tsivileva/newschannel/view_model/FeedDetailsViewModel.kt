package com.hfad.news.tsivileva.newschannel.view_model

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteFeedsDetails
import io.reactivex.disposables.Disposable

class FeedDetailsViewModel : ViewModel() {
  private val repository = RemoteFeedsDetails()

    val cached=repository.cachedList
    val newsStore = repository.newsStore
    val isDownloadSuccessful = repository.isDownloadSuccessful

     var subscription:Disposable?=null

    fun loadContent(url: String) {
        val _news=findNewsInCache(url)
        if(_news==null){
            subscription=repository.downloadFeedsDetails(url)
            logIt("FeedDetailsViewModel","loadContent","загузка из сети ")

        }else{
            logIt("FeedDetailsViewModel","loadContent","загузка из кэша ")

            newsStore.postValue(_news)
            isDownloadSuccessful.postValue(true)
        }

    }

    fun findNewsInCache(url:String):NewsItem?{
     val id= getIdInLink(url)
     val source= getFeedsSource(url)
       return repository.cachedList.find { it.id == id && it.sourceKind == source}
    }

    fun stopLoad() {
       subscription?.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        stopLoad()
    }

    fun refreshData(){
     repository.cleareCache()
    }
}