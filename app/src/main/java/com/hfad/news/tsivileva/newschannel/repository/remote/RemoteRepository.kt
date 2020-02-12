package com.hfad.news.tsivileva.newschannel.repository.remote

import android.util.Log
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.tproger.TProger
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class RemoteRepository {
    private val HABR_URL = "https://habr.com/ru/rss/all/"
    private val PROGER_URL = "https://tproger.ru/feed/"

    private fun createRetrofit(baseUrl: String, type: FactoryTypes): Retrofit {
        val factory: Converter.Factory = when (type) {
            FactoryTypes.SIMPLE_XML -> SimpleXmlConverterFactory.create()
            FactoryTypes.JSPOON -> JspoonConverterFactory.create()
        }
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(OkHttpClient.Builder().build())
                .build()
    }

    private fun getAllNews() {
        val habrObservable = createRetrofit(HABR_URL, FactoryTypes.SIMPLE_XML)
                                .create(INetwork::class.java)
                                .loadNews()
        val progerObservable = createRetrofit(PROGER_URL, FactoryTypes.SIMPLE_XML)
                                .create(INetwork::class.java)
                                .loadTProger()

        val observable: Observable<Any> = Observable.merge(habrObservable, progerObservable)

        val observer = object : DisposableObserver<Any>() {

            override fun onComplete() {
                Log.d("mylog", "onComplete")
            }

            override fun onNext(t: Any) {
/*
                when (t) {
                    is TProger -> {
                        t.channel.item.forEach {
                            var newsItem = NewsItem()
                            newsItem.date = it.pubDate
                            newsItem.picture = "https://pbs.twimg.com/profile_images/857551974442651648/D5cZLXTf.jpg";
                            newsItem.summarry = it.description
                            newsItem.title = it.title
                            newsItem.link = it.link
                            Log.d("mylog", "onNext-habr")
                            addNews(newsItem)
                        }
                    }
                    is Habr -> {
                        t.habrlItems?.forEach {
                            var newsItem = NewsItem()
                            newsItem.date = it.date
                            newsItem.link = it.link
                            newsItem.title = it.title
                            newsItem.summarry = it.habrItemsDetail?.description
                            newsItem.picture = it.habrItemsDetail?.imageSrc
                            Log.d("mylog", "onNext-Habr, image = ${newsItem.picture}, description=${newsItem.summarry} ")
                            addNews(newsItem)
                        }
                    }
                }*/
            }

            override fun onError(e: Throwable) {
                Log.d("mylog", "error ${e.message}")
            }

        }
        //subscription = observable.subscribeWith(observer)
    }

}