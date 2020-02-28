package com.hfad.news.tsivileva.newschannel.repository.remote


import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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
        fun getProgerObservable(): Observable<Proger> {
            return createService(FeedsSource.PROGER.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java).loadProger().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        fun getHabrObservable(): Observable<Habr> {
            return createService(FeedsSource.HABR.link, SimpleXmlConverterFactory.create(), IRemoteApi::class.java).loadHabr().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        fun getProgerFeedsContentObservable(url: String): Observable<ProgerContent> {
            return createService(url, JspoonConverterFactory.create(), IRemoteApi::class.java).loadProgDetails()
        }

        fun getHabrFeedsContentObservable(url: String): Observable<HabrContent> {
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
    }
}
