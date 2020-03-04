package com.hfad.news.tsivileva.newschannel.repository.remote


import android.util.Log
import androidx.lifecycle.LiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.repository.local.News
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
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

       fun downloadingFromInternet(database: LocalDatabase?, url: String, id:Long): LiveData<NewsAndContent>? {
           var _news: LiveData<NewsAndContent>? = null

           if (getSourceByLink(url) == FeedsSource.HABR) {
               RemoteRepository.getHabrContentObservable(url)
                       .map { database?.getLocalRepo()?.insertContent(it) }
                       .map { database?.getLocalRepo()?.selectNewsAndContent(it) }
                       .doOnSuccess {_news=it
                           Log.d(DEBUG_LOG,"source ${it.toString()}")
                       }
                       .doOnError { e -> e.printStackTrace()
                           database?.getLocalRepo()?.selectNewsAndContent(id)}
                       .subscribeOn(Schedulers.io()).subscribe()
           } else {
              if (getSourceByLink(url) == FeedsSource.PROGER){
                  RemoteRepository.getProgerContentObservable(url)
                          .map { database?.getLocalRepo()?.insertContent(it) }
                          .map {  database?.getLocalRepo()?.selectNewsAndContent(it) }
                          .doOnSuccess {it->_news=it}
                          .doOnError { e -> e.printStackTrace()
                              database?.getLocalRepo()?.selectNewsAndContent(id)}
                                      .subscribeOn(Schedulers.io())
                                      .subscribe()

              }

           }
       return _news
       }



        fun getFeedsObservable(): Observable<List<News>> {
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
                    }).flatMap (::parseFeed)
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
