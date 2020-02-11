package com.hfad.news.tsivileva.newschannel.network

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


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


