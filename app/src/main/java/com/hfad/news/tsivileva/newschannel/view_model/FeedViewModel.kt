package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.model.local.NewsDescription
import com.hfad.news.tsivileva.newschannel.repository.local.NewsDatabase
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class FeedViewModel(val app: Application) : AndroidViewModel(app) {

    private var subscription: Disposable? = null

    var downloading = MutableLiveData<DownloadingState<List<NewsDescription>>>()

    fun downloadFeeds() {
        subscription = RemoteRepository
                .getFeedsObservable()
                .subscribe(::onNext, ::onError, ::onComplete)
    }

    private fun onComplete() {
        val list = NewsDatabase.instance(getApplication())?.getApi()?.selectAllDescriptions()
        if (list != null) {
            downloading.postValue(DownloadingSuccessful(list))
        }
    }

    private fun onError(e: Throwable) {
        NewsDatabase.instance(getApplication())?.getApi()
                ?.selectAllDescriptions()
                ?.let { cachedList: List<NewsDescription> ->
                    downloading.postValue(DownloadingError(e, cachedList))
                }
    }

    private fun onNext(item: List<NewsDescription>) {
        NewsDatabase.instance(getApplication())?.getApi()?.insertIntoDescription(item)
    }

    fun sortNews(sortTypeKind: SortType, source: FeedsSource) {
        var list :List<NewsDescription>?= listOf<NewsDescription>()
        when (source) {
            FeedsSource.HABR, FeedsSource.PROGER -> {
                when (sortTypeKind) {
                    SortType.ASC -> list=NewsDatabase.instance(getApplication())?.getApi()?.selectSortedByDateAsc(source)
                    SortType.DESC ->list=NewsDatabase.instance(getApplication())?.getApi()?.selectDescriptionByDateDesc(source)
                }
            }
            FeedsSource.BOTH -> {
                when (sortTypeKind) {
                    SortType.ASC ->list=NewsDatabase.instance(getApplication())?.getApi()?.selectAllSortedByDateAsc()
                    SortType.DESC ->list=NewsDatabase.instance(getApplication())?.getApi()?.selectDescriptionSortedByDateDesc()
                }
            }
        }
        if(list!=null)  downloading.postValue(DownloadingSuccessful(list) )
    }

    override fun onCleared() {
        NewsDatabase.destroyInstance()
        subscription?.dispose()
        super.onCleared()
    }

    fun searchByTitle(title: String) {
      val list= NewsDatabase.instance(getApplication())?.getApi()?.selectDescriptionByTitle("%$title%")
        if(list!=null)  downloading.postValue(DownloadingSuccessful(list) )
    }
}