package com.hfad.news.tsivileva.newschannel

import io.reactivex.Observable
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class RemoteRepositoryFeedsFactoryTest {

    val list = mutableListOf<String>("1", "2", "3")

    @Test
    fun remote_factory_test() {
        val obsevable = Observable.fromArray(list)
        obsevable.map(::mapper).subscribe()
    }

    fun mapper(i: MutableList<String>): String {
        var str = ""
        i.forEach {
            str += "\n ${(it.toInt() + 100)}"
            println(str)
        }
        return str
    }
}