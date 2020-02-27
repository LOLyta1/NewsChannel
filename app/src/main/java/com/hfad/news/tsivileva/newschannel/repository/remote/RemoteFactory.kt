package com.hfad.news.tsivileva.newschannel.repository.remote


import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.DisposableObserver

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory


class RemoteFactory private constructor() {

    companion object {
       fun getProgerObservable():Observable<Proger>{
            return createService(FeedsSource.PROGER.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java).loadProger()
        }
        fun getHabrObservable():Observable<Habr>{
            return createService(FeedsSource.HABR.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java).loadHabr()
        }
        fun getProgerContentObservable(url:String): Observable<ProgerContent> {
            return createService(url, JspoonConverterFactory.create(), IRemoteApi::class.java).loadProgDetails()
        }

        fun getHabrContentObservable(url:String):Observable<HabrContent>{
            return createService(url, JspoonConverterFactory.create(), IRemoteApi::class.java).loadHabrContent()
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

         private fun <T>  createObserver(_onNext: (item: T) -> Unit, _onComplete: () -> Unit, _onError: (e: Throwable) -> Unit): DisposableObserver<T> {
            return object : DisposableObserver<T>() {
                override fun onComplete() = _onComplete()
                override fun onNext(t: T) = _onNext(t)
                override fun onError(e: Throwable) = _onError(e)
            }
        }
    }
}
