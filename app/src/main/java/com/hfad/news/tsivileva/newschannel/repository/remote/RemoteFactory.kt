package com.hfad.news.tsivileva.newschannel.repository.remote

import com.hfad.news.tsivileva.newschannel.model.IModel
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class RemoteFactory private constructor() {

  companion object{
      fun <T> createService(baseUrl: String, converterFactory: Converter.Factory, _class: Class<T>): T {
          val retrofit = Retrofit.Builder()
                  .baseUrl(baseUrl)
                  .addConverterFactory(converterFactory)
                  .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                  .client(OkHttpClient.Builder().build())
                  .build()
          return retrofit.create(_class)
      }



       private fun <T> subscribe (service: IRemoteApi, model: IModel, _onNext: (model:IModel) -> Unit, _onComplete: () -> Unit, _onError: (e:Throwable) -> Unit): Disposable? {
               when (model) {
                is Habr -> {
                    val observable= service.loadHabr().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                    val observer= getObserver<Habr>(_onNext,_onComplete,_onError)
                    return observable.subscribeWith(observer)
                }
                is Proger -> {
                    val observable= service.loadProger().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                    val observer= getObserver<Proger>(_onNext,_onComplete,_onError)
                    return observable.subscribeWith(observer)
                }
                is HabrContent -> {
                    val observable= service.loadHabrContent().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                    val observer= getObserver<HabrContent>(_onNext,_onComplete,_onError)
                    return observable.subscribe(_onNext,_onError)
                }
                is ProgerContent -> {
                    val observable= service.loadProgDetails().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                    val observer= getObserver<ProgerContent>(_onNext,_onComplete,_onError)
                    return observable.subscribe(_onNext,_onError)
                }
                else -> return null
            }
        }


        private fun <T> getObserver(_onNext: (T) -> Unit, _onComplete: () -> Unit, _onError: (e:Throwable) -> Unit): DisposableObserver<T> {
            return object : DisposableObserver<T>() {
                override fun onComplete() {
                    _onComplete()
                }

                override fun onNext(t: T) {
                    _onNext(t)
                }

                override fun onError(e: Throwable) {
                    _onError(e)
                }

            }
        }
    }

}
