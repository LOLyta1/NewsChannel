package com.hfad.news.tsivileva.newschannel.repository.remote


import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem

import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.view_model.Sort
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction



class RemoteFeeds {

    var cache = mutableListOf<NewsItem>()
    val isDownloadSuccessful = MutableLiveData<Boolean>()
    val newsStore = MutableLiveData<MutableList<NewsItem>>()


    //загрузка из одного источника
    fun downloadFeeds(source: FeedsSource): Disposable {
        logIt("RemoteFeed","downloadFeeds()","url - $source", DEBUG_LOG)

        return when (source) {
            FeedsSource.HABR -> {
                logIt("RemoteFeed","downloadFeeds()","HABR", DEBUG_LOG)
                return RemoteFactory
                        .getHabrObservable()
                        .map(::parsHabrFeed)
                        .subscribe(::onNext,::onError,::onComplete)

            }

           FeedsSource.PROGER -> {
                logIt("RemoteFeed","downloadFeeds()","PROGER", DEBUG_LOG)
                return RemoteFactory
                        .getProgerObservable()
                        .map(::parsProgerFeed)
                        .subscribe(::onNext,::onError,::onComplete)

             }
            FeedsSource.BOTH -> {
                logIt("RemoteFeed","downloadFeeds()","BOTH", DEBUG_LOG)
                val observable=Observable.zip(
                        RemoteFactory.getHabrObservable(),
                        RemoteFactory.getProgerObservable(),
                        BiFunction<Habr, Proger, MutableList<List<Any>?>> {
                            h, p ->mutableListOf(h.items, p.channel?.items)
                        }
                )
                observable.map(::parseFeed).subscribe(::onNext,::onError,::onComplete)
            }
        }

    }

    fun onComplete() {
        logIt("RemoteFeed","onComplete()","", DEBUG_LOG)
        sortNewsList(cache, Sort.BY_DATE_ASC)
        newsStore.postValue(cache)
        isDownloadSuccessful.postValue(true)
    }

    fun onError(e: Throwable) {
        logIt("RemoteFeed","onError()","", DEBUG_LOG)
        isDownloadSuccessful.postValue(false)
    }

    fun onNext(item : List<NewsItem>){
        logIt("RemoteFeed","onNext()","", DEBUG_LOG)
        cache.addAll(item)
        printCachedMutableList("","",cache)
    }

    fun sortNews(sortKind: Sort) {
        sortNewsList(cache, sortKind)
        newsStore.postValue(cache)
        isDownloadSuccessful.postValue(true)
    }
}