package com.hfad.news.tsivileva.newschannel.network

import com.hfad.news.tsivileva.newschannel.Model.implementation.habr.HabrItemsDetailConverter
import com.hfad.news.tsivileva.newschannel.Model.implementation.habr.HabrItemsInfo
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.convert.Registry
import org.simpleframework.xml.convert.RegistryStrategy
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.strategy.Strategy
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory


class NetworkClientHabrDetails {

        fun instance(url :String?): Retrofit {
            /*адаптор для выполнения запросов не в GUI-потоке, а через планировщик*/
            val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

                val builder = OkHttpClient.Builder()
                val okHttpClient = builder.build()

               return Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(JspoonConverterFactory.create())
                        .addCallAdapterFactory(rxAdapter)
                        .client(okHttpClient)
                        .build()
        }
    }


