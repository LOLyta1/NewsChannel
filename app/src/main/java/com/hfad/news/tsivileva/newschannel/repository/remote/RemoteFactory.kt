package com.hfad.news.tsivileva.newschannel.repository.remote

import android.os.Build
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.model.IModel
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.*


class RemoteFactory private constructor() {


    interface IRemoteRepositoryEventListener{
        fun setOnNextParser(parser){}
    }


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

      fun  getObserver(service: IRemoteApi, obj : IModel) :  Any?{
          when(obj){
              is Habr->{return service.loadHabr().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()) }
              is Proger->{return service.loadProger().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()) }
              is HabrContent->{return service.loadHabrDetails().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()) }
              is ProgerContent->{return service.loadProgDetails().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()) }
              else -> return null
          }
      }



      fun <T> getObservable(_class:Class<T>,_onNext: (T) -> Unit):DisposableObserver<T>{
          return object:DisposableObserver<T>(){
              override fun onComplete() {

               }

              override fun onNext(t: T) {
                  _onNext(t)
              }

              override fun onError(e: Throwable) {
                  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
              }

          }
      }
  }




 /*   fun <T> createObserver(): DisposableObserver<T> {
        val observer= object : DisposableObserver<T>() {
            override fun onComplete() {
                _onCompleteFunctor
            }

            override fun onError(e: Throwable) {
              _onErrorFunction
            }

            override fun onNext(t: T) {
                _onNextFunctor
            }
        }
        return observer
    }*/


}
