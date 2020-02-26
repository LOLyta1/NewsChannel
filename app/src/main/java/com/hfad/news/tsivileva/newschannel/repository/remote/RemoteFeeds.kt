package com.hfad.news.tsivileva.newschannel.repository.remote


import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.Source
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.view_model.Sort
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers


import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.*

class RemoteFeeds {

    private var newsCache = mutableListOf<NewsItem>()
    val isDownloadSuccessful = MutableLiveData<Boolean>()
    val newsStore = MutableLiveData<MutableList<NewsItem>>()


    //загрузка из одного источника
    fun downloadFeeds(url: String): Disposable {
        var service = RemoteFactory.createService(url, SimpleXmlConverterFactory.create(), IRemoteApi::class.java)
        return when (getSourceKind(url)) {
            Source.HABR -> {
                val observable = service.loadHabr().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                return observable.map(::parsHabrFeed).subscribe(::onNext,::onError,::onComplete)
            }
            Source.PROGER -> {
                val observable = service.loadProger().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                return observable.map(::parsProgerFeed).subscribe(::onNext,::onError,::onComplete)
             }
            else -> {
                var service = RemoteFactory.createService(Source.HABR.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java)
                val habrObservable=service.loadHabr().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())

                service = RemoteFactory.createService(Source.PROGER.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java)
                val progerObservable=service.loadProger().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())

                val observable=Observable.zip(habrObservable, progerObservable, BiFunction<Habr, Proger, MutableList<Any?>> { h, p ->
                    mutableListOf(h.items, p.channel?.items)})
                observable.map(::parseFeed).subscribe()
            }
            //сливаем 2 потока
        }

    }

    fun onComplete() {
        newsStore.postValue(newsCache)
        isDownloadSuccessful.postValue(true)
    }

    fun onError(e: Throwable) {
        isDownloadSuccessful.postValue(false)
    }

    fun onNext(item : List<NewsItem>){
        newsCache.addAll(item)
    }

    fun cleareCache() {
        newsCache = mutableListOf()
    }

    fun sortNews(sortKind: Sort) {
        sortNewsList(newsCache, sortKind)
        newsStore.postValue(newsCache)
        isDownloadSuccessful.postValue(true)
    }
}