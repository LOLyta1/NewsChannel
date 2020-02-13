package com.hfad.news.tsivileva.newschannel.repository.remote

import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrItemsInfo
import com.hfad.news.tsivileva.newschannel.model.tproger.TProger
import com.hfad.news.tsivileva.newschannel.model.tproger.TProgerItemsInfo
import io.reactivex.Flowable
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

class RemoteRepository {

          val HABR_URL = "https://habr.com/ru/rss/all/"
          val PROGER_URL = "https://tproger.ru/feed/"


    private fun createRetrofit(baseUrl: String, type: FactoryTypes): Retrofit {
        val factory: Converter.Factory = when (type) {
            FactoryTypes.SIMPLE_XML -> SimpleXmlConverterFactory.create()
            FactoryTypes.JSPOON -> JspoonConverterFactory.create()
        }
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                //.addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(OkHttpClient.Builder().build())
                .build()
    }

     fun createObservableAllNews() :Observable<MutableList<List<Any>?>> {
        val habrFlowable = createRetrofit(HABR_URL, FactoryTypes.SIMPLE_XML)
                                .create(INetwork::class.java)
                                .loadHabr()
                                .observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread());

        val progerFlowable = createRetrofit(PROGER_URL, FactoryTypes.SIMPLE_XML)
                                .create(INetwork::class.java)
                                .loadProger()
                                .observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread());

        return Observable.zip(habrFlowable,progerFlowable, BiFunction<Habr, TProger,MutableList<List<Any>?>> {
            h,p->
            mutableListOf(h.items,p.channel?.items)
        })
     }


     fun createFlowableHabrItem(url:String) :Observable<HabrItemsInfo>{
        return createRetrofit(url, FactoryTypes.SIMPLE_XML)
                .create(INetwork::class.java)
                .loadHabrDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

     fun createFlowableProgerItem(url: String):Observable<TProgerItemsInfo>{
        return createRetrofit(url, FactoryTypes.SIMPLE_XML)
                .create(INetwork::class.java)
                .loadProgDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
     }

}