package com.hfad.news.tsivileva.newschannel.repository.remote


import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.Source
import com.hfad.news.tsivileva.newschannel.getIdInLink
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.parseFeed
import com.hfad.news.tsivileva.newschannel.sortNewsList
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
        return when (url) {
            Source.HABR.link -> {
                //flatmap|map
                val observable = service.loadHabr().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
               val observer = RemoteFactory.createObserver<Habr>(::parseFeed, ::onComplete, ::onError)
                observable.subscribe(::parseFeed,::onError,::onComplete)
            }
            Source.PROGER.link -> {
                val observable = service.loadProger().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                val observer = RemoteFactory.createObserver<Proger>(::parseFeeds, ::onComplete, ::onError)
                observable.subscribeWith(observer)
             }
            else -> {

                val habrObserervable=service.loadHabr().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                val progerObservable=service.loadProger().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                val observable= Observable.zip(habrObserervable,progerObservable, BiFunction <Habr,Proger, MutableList<Any?>> { h, p -> parseFeeds()})
                observable.subscribe{}

                 val a=0
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

    fun cleareCache() {
        newsCache = mutableListOf()
    }

    fun sortNews(sortKind: Sort) {
        sortNewsList(newsCache, sortKind)
        newsStore.postValue(newsCache)
        isDownloadSuccessful.postValue(true)
    }
}