package com.hfad.news.tsivileva. newschannel.repository.remote


import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.model.ModelConverter
import com.hfad.news.tsivileva.newschannel.model.local.Content
import com.hfad.news.tsivileva.newschannel.model.local.Description
import com.hfad.news.tsivileva.newschannel.model.remote.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.remote.proger.Proger
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class RemoteRepository {
    companion object Factory {
        fun getFeedsObservable(): Observable<List<Description>> {
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
                    }).flatMap(ModelConverter()::toNewsDesciption)
        }

        fun getProgerContentObservable(url: String): Single<Content> {
            return createService(url, JspoonConverterFactory.create(), IRemoteApi::class.java)
                    .loadProgDetails()
                    .map(ModelConverter()::toNewsContent)
        }


        fun getHabrContentObservable(url: String): Single<Content> {
            return createService(url, JspoonConverterFactory.create(), IRemoteApi::class.java)
                    .loadHabrContent()
                    .map(ModelConverter()::toNewsContent)
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

        fun getDownloadingObservable(filePath: String, url: String): Observable<Long>? {
            var inputStream: InputStream?=null
            var outputFile: FileOutputStream?=null

            return  Observable.create { source: ObservableEmitter<Long> ->
                try {
                    val response = OkHttpClient().newCall(Request.Builder().url(url).build()).execute()
                    if (response.isSuccessful) {
                        inputStream = response.body?.byteStream()
                        outputFile = FileOutputStream(filePath)
                        val dataBuffer = ByteArray(1024)

                        val contentLength = response.body?.contentLength()
                        var offset = 0
                        var count: Int?=0

                        source.onNext(0)/*информируем подписчика, что скачалось 0 процентов*/

                        while (count!=-1) {

                            //if(count==0) source.onComplete()
                            if (count!=null && count!=0 && contentLength != null) {
                                offset += count
                                source.onNext( (offset*100)/contentLength)
                                outputFile?.write(dataBuffer, 0, count)
                            } else {
                              //  source.onComplete()
                            }
                            count=inputStream?.read(dataBuffer)
                        }
                        source.onComplete()
                        outputFile?.flush()
                    }
                } catch (e: Exception) {
                    source.onError(e)
                } finally {
                 try {
                     inputStream?.close()
                     outputFile?.close()
                 } catch (e: IOException){
                     e.printStackTrace()
                 }
                }
            }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
        }
    }

}
