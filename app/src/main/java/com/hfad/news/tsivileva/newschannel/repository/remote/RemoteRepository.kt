package com.hfad.news.tsivileva.newschannel.repository.remote


import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import com.hfad.news.tsivileva.newschannel.parseFeed
import com.hfad.news.tsivileva.newschannel.parseHabrFeedsContent
import com.hfad.news.tsivileva.newschannel.parseProgerFeedsContent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory


class RemoteRepository private constructor() {

    companion object Factory {

        fun getFeedsObservable(): Observable<MutableList<NewsItem>> {
            var service = createService(FeedsSource.PROGER.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java)
            val proger = service
                    .loadProger()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

            service = createService(FeedsSource.HABR.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java)
            val habr = service
                    .loadHabr()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

            return Observable.zip(habr, proger,
                    BiFunction<Habr, Proger, MutableList<List<Any>?>> { h, p ->
                        mutableListOf(h.items, p.channel?.items)
                    }).map(::parseFeed)
        }


        fun getProgerContentObservable(url: String): Observable<NewsItem> {
            return createService(url, JspoonConverterFactory.create(), IRemoteApi::class.java)
                    .loadProgDetails()
                    .map(::parseProgerFeedsContent)
        }

        fun getHabrContentObservable(url: String): Observable<NewsItem> {
            return createService(url, JspoonConverterFactory.create(), IRemoteApi::class.java)
                    .loadHabrContent()
                    .map(::parseHabrFeedsContent)
        }

        private fun <T> createService(baseUrl: String, converterFactory: Converter.Factory, _class: Class<T>): T {
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(OkHttpClient.Builder().build())
                    .build()
            return retrofit.create(_class)
        }

    }
}
