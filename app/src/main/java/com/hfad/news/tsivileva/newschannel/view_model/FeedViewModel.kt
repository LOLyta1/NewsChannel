package com.hfad.news.tsivileva.newschannel.view_model

import android.graphics.ImageDecoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.repository.*
import com.hfad.news.tsivileva.newschannel.repository.remote.FeedsRepository
import io.reactivex.disposables.Disposable


class FeedViewModel : ViewModel() {
    private val repository = FeedsRepository()
    private var subscription : Disposable?=null
    private var newsList= mutableListOf<NewsItem>()

     var downloadingNews= MutableLiveData<DownloadingState>()

    fun downloadFeeds() {
        if (newsList.isEmpty()) {
           subscription= repository.getObservableFeeds().subscribe(::onNext,::onError,::onComplete)
        } else {
            downloadingNews.value=DownloadingSuccessful(newsList)
        }
    }

    fun cleareCache() {
      newsList = mutableListOf()
    }

    private fun onComplete() {
       sortNews(Sort.BY_ABC_ASC)
       downloadingNews.postValue(DownloadingSuccessful(newsList))
       subscription?.dispose()
    }

    private fun onError(e: Throwable) {
        downloadingNews.postValue(DownloadingError(e))
    }

    private fun onNext(item: List<NewsItem>) {
        if (!newsList.containsAll(item)) {
            newsList.addAll(item)
        }
        downloadingNews.postValue(DownloadingProgress(""))
    }


    fun sortNews(sortKind: Sort) {
        when (sortKind) {
            Sort.BY_ABC_ASC -> {
                newsList.sortBy { it.title }
            }
            Sort.BY_ABC_DESC -> {
                newsList.sortByDescending { it.title }
            }
            Sort.BY_DATE_ASC -> {
                newsList.sortBy { it.date }
            }
            Sort.BY_DATE_DESC -> {
                newsList.sortByDescending { it.date }
            }
        }
    }

    fun filterNews(sourceKind: FeedsSource): List<NewsItem> {
        val _tempList = newsList.filter { it.sourceKind == sourceKind }
        return _tempList
    }
}