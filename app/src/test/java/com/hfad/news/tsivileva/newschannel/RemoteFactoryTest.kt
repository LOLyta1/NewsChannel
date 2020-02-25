package com.hfad.news.tsivileva.newschannel

import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.repository.remote.IRemoteApi
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import org.junit.Test
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class RemoteFactoryTest {


    @Test
    fun remote_factory_test() {
      fun onNextFunc(){print("onNext")}
      fun onCompleteFunc(){print("onComplete")}
      fun onErrorFunc(){print("onError")}

        val factory = RemoteFactory.
                        Builder().
                        onCompleteFunction(::onCompleteFunc).
                        onErrorFunction(::onErrorFunc).
                        onNextFunction(::onNextFunc).
                        build()

        //1.create service
        //2. pass the method name to work with API
        //3. with method create observer
        val serviceAPI=factory.createService("https://habr.com/ru/rss/all/",SimpleXmlConverterFactory.create(), IRemoteApi::class.java)

        val observer=factory.createObserver<Habr>()

        val observable: Observable<Habr> =serviceAPI.loadHabr().observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        //  val api=service.

        observable.subscribeWith(observer)

    }


}