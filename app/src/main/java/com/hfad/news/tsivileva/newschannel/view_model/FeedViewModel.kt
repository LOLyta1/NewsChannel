package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.getViewModelFactory
import com.hfad.news.tsivileva.newschannel.repository.DownloadedFeeds
import com.hfad.news.tsivileva.newschannel.repository.DownloadingError
import com.hfad.news.tsivileva.newschannel.repository.DownloadingProgress
import com.hfad.news.tsivileva.newschannel.repository.DownloadingState
import com.hfad.news.tsivileva.newschannel.repository.local.LocalDatabase
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable


class FeedViewModel(val app: Application) : AndroidViewModel(app) {

    private var subscription: Disposable? = null
    private var newsList = mutableListOf<NewsItem>()
    private var database:LocalDatabase? = LocalDatabase.instance(getApplication())

    var downloading = MutableLiveData<DownloadingState>()

    fun downloadFeeds() {
        if (newsList.isEmpty()) {
            subscription = RemoteRepository
                    .getFeedsObservable()
                    .subscribe(::onNext, ::onError, ::onComplete)
        } else {
            downloading.value = DownloadedFeeds(newsList)
        }
    }

    fun cleareCache() {
        newsList = mutableListOf()
    }

    private fun onComplete() {
        newsList = sortNews(Sort.BY_ABC_ASC, newsList)
        downloading.postValue(DownloadedFeeds(newsList))
        subscription?.dispose()
    }

    private fun onError(e: Throwable) {
        downloading.postValue(DownloadingError(e))
    }

    private fun onNext(item: List<NewsItem>) {
        if (!newsList.containsAll(item)) {
            newsList.addAll(item)
        }
        database?.getLocalRepo()?.insert(newsList)
        downloading.postValue(DownloadingProgress(""))
    }


    fun sortNews(sortKind: Sort, list: MutableList<NewsItem>): MutableList<NewsItem> {
        val _list = list
        when (sortKind) {
            Sort.BY_ABC_ASC -> {
                _list.sortBy { it.title }
            }
            Sort.BY_ABC_DESC -> {
                _list.sortByDescending { it.title }
            }
            Sort.BY_DATE_ASC -> {
                _list.sortBy { it.date }
            }
            Sort.BY_DATE_DESC -> {
                _list.sortByDescending { it.date }
            }
        }
        return _list
    }

    fun filterNews(sourceKind: FeedsSource): List<NewsItem> {
        val _tempList: List<NewsItem>

        if (sourceKind == FeedsSource.BOTH) {
            _tempList = newsList.filter { it.sourceKind == FeedsSource.HABR || it.sourceKind == FeedsSource.PROGER }
        } else {
            _tempList = newsList.filter { it.sourceKind == sourceKind }
        }

        return _tempList
    }

    fun searchByTitle(title: String) = newsList.filter { it.title.contains(title) }

    override fun onCleared() {
        LocalDatabase.destroyInstance()
        super.onCleared()
    }


}