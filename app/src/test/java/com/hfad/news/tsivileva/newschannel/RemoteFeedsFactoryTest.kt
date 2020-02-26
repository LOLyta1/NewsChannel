package com.hfad.news.tsivileva.newschannel

import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.repository.remote.IRemoteApi
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteFactory
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import org.junit.Test
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class RemoteFeedsFactoryTest {

val list= mutableListOf<String>("1","2","3")

    @Test
    fun remote_factory_test() {
        val obsevable=Observable.fromArray(list)
        obsevable.map(::mapper).subscribe()
    }
    fun mapper(i:MutableList<String>) : String{
        var str=""
        i.forEach{
            str+="\n ${(it.toInt()+100)}"
            println(str)
        }
       return str
    }
}