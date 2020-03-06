package com.hfad.news.tsivileva.newschannel.repository.remote


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.repository.local.NewsDescription
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import com.hfad.news.tsivileva.newschannel.repository.local.LocalDatabase
import com.hfad.news.tsivileva.newschannel.repository.local.NewsAndContent
import com.hfad.news.tsivileva.newschannel.repository.local.NewsContent
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory


class RemoteRepository {


    companion object Factory {

        fun getFeedsObservable(): Observable<List<NewsDescription>> {
            var service = createService(FeedsSource.PROGER.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java)
            val proger = service
                    .loadProger()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())

            service = createService(FeedsSource.HABR.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java)
            val habr = service
                    .loadHabr()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())

            return Observable.zip(habr, proger,
                    BiFunction<Habr, Proger, List<List<Any>?>> { h, p ->
                        listOf(h.items, p.channel?.items)
                    }).flatMap(::parseFeed)
        }


        fun getProgerContentObservable(url: String): Single<NewsContent> {
            return createService(url, JspoonConverterFactory.create(), IRemoteApi::class.java)
                    .loadProgDetails()
                    .map(::parseProgerFeedsContent)
        }


        fun getHabrContentObservable(url: String): Single<NewsContent> {
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
