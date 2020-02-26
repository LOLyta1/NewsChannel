package com.hfad.news.tsivileva.newschannel.repository.remote

import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.Source
import com.hfad.news.tsivileva.newschannel.getSourceKind
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.disposables.Disposable
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class RemoteFeedsDetails {

    var newsStore = MutableLiveData<NewsItem>()
    val isDownloadSuccessful = MutableLiveData<Boolean>()
    var cachedFeed: NewsItem?=null


    //ниже - дзагрузка из 1 источника
    fun downloadFeedsDetails(url:String): Disposable?{
        var service = RemoteFactory.createService(url, SimpleXmlConverterFactory.create(), IRemoteApi::class.java)
        return when (getSourceKind(url)) {
            Source.HABR -> {
                val observable = service.loadHabrContent()
                val observer = RemoteFactory.createObserver<HabrContent>(::parseHabrFeedsDetails, ::onComplete, ::onError)
                return observable.subscribeWith(observer)
            }
            Source.PROGER ->{
                val observable = service.loadProgDetails()
                val observer = RemoteFactory.createObserver<ProgerContent>(::parseProgerFeedsDetails, ::onComplete, ::onError)
                return observable.subscribeWith(observer)
            }
            else -> return null
        }
    }

    fun parseHabrFeedsDetails(hc: HabrContent)  {
        cachedFeed= NewsItem(
                title = hc.title,
                content = hc.content,
                date = hc.date,
                picture = hc.image,
                link = hc.link,
                id = hc.id,
                sourceKind = Source.HABR
        )

    }

    fun parseProgerFeedsDetails(pc: ProgerContent) {
       cachedFeed= NewsItem(
                title = pc.title,
                content = pc.content,
                date = pc.date,
                picture =pc.image,
                link = pc.link,
                id = pc.id,
                sourceKind = Source.PROGER)

    }

    fun onComplete() {
        isDownloadSuccessful.postValue(true)
    }

    fun onError(e: Throwable) {
        isDownloadSuccessful.postValue(false)

    }
}