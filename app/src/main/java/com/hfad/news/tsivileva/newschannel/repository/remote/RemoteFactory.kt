package com.hfad.news.tsivileva.newschannel.repository.remote

import androidx.annotation.Nullable
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.model.IModel
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class RemoteFactory private constructor() {

    companion object {
        fun <T> createService(baseUrl: String, converterFactory: Converter.Factory, _class: Class<T>): T {
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(OkHttpClient.Builder().build())
                    .build()
            return retrofit.create(_class)
        }

         fun <T>  createObserver(_onNext: (item: T) -> Unit, _onComplete: () -> Unit, _onError: (e: Throwable) -> Unit): DisposableObserver<T> {
            return object : DisposableObserver<T>() {
                override fun onComplete() = _onComplete()
                override fun onNext(t: T) = _onNext(t)
                override fun onError(e: Throwable) = _onError(e)
            }
        }
         fun combineObserbers(
                observers: List<Observable<IModel>>,
                function: (t: Array<Any>) -> MutableList<List<Any>?>)
                :Observable<MutableList<List<Any>?>> {
            return Observable.zip(observers, function)
        }
    }
}
