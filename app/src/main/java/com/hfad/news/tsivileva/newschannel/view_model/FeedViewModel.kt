package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.repository.*
import com.hfad.news.tsivileva.newschannel.repository.local.NewsDescription
import com.hfad.news.tsivileva.newschannel.repository.local.LocalDatabase
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class FeedViewModel(val app: Application) : AndroidViewModel(app) {

    private var subscription: Disposable? = null
    //   private var newsList = mutableListOf<NewsItem>()
    private var database: LocalDatabase? = LocalDatabase.instance(getApplication())

    var downloading = MutableLiveData<DownloadingState<List<NewsDescription>>>()

    fun downloadFeeds() {
        subscription = RemoteRepository
                .getFeedsObservable()
                .subscribe(::onNext, ::onError, ::onComplete)
    }

    fun cleareCache() {
        //  newsList = mutableListOf()
    }

    private fun onComplete() {
        val list = database?.getLocalRepo()?.selectAllNews()
        if (list != null) {
            downloading.postValue(DownloadingSuccessful(list))
        }
    }

    private fun onError(e: Throwable) {
        val list = database
                ?.getLocalRepo()
                ?.selectAllNews()
                ?.let{ cachedList:List<NewsDescription>->
                    downloading.postValue(DownloadingError(e,cachedList))
                }
    }

    private fun onNext(item: List<NewsDescription>) {
        database?.getLocalRepo()?.insertIntoNews(item)
    }

    fun sortNews(sortKind: Sort, source: FeedsSource) {
        val observable: Observable<List<NewsDescription>>?
        when (source) {
            FeedsSource.HABR, FeedsSource.PROGER -> {
                when (sortKind) {
                    Sort.ASC -> observable = database?.getLocalRepo()?.selectSortedByDateAsc(source)
                    Sort.DESC -> observable = database?.getLocalRepo()?.selectSortedByDateDesc(source)
                }
            }
            FeedsSource.BOTH -> {
                when (sortKind) {
                    Sort.ASC -> observable = database?.getLocalRepo()?.selectAllSortedByDateAsc()
                    Sort.DESC -> observable = database?.getLocalRepo()?.selectAllSortedByDateDesc()
                }
            }
        }
        subscription = observable
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnNext {

                    _list ->
                    downloading.postValue(DownloadingSuccessful(_list))
                    _list.forEach {
                        //Log.d(DEBUG_LOG, "doOnNext()  - ${it.date}.${it.title},content - ${it.content}")
                    }
                }
                ?.doOnComplete { subscription?.dispose() }
                ?.doOnError { e -> e.printStackTrace() }
                ?.subscribe()
    }

    override fun onCleared() {
        LocalDatabase.destroyInstance()
        subscription?.dispose()
        super.onCleared()
    }

    fun searchByTitle(title: String) {
        subscription = database?.getLocalRepo()?.selectNewsByTitle("%$title%")
                ?.doOnSuccess {  downloading.postValue(DownloadingSuccessful(it))}
                ?.doOnError { e -> e.printStackTrace() }
                ?.subscribeOn(Schedulers.io())
                ?.subscribe()
    }
}