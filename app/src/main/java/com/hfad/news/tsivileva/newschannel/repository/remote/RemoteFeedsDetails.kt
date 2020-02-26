package com.hfad.news.tsivileva.newschannel.repository.remote

import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.FeedsContentSource
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.getFeedsContentSource
import com.hfad.news.tsivileva.newschannel.getFeedsSource
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.disposables.Disposable

class RemoteFeedsDetails {

    var newsStore = MutableLiveData<NewsItem>()

    val isDownloadSuccessful = MutableLiveData<Boolean>()
    var cachedFeed: NewsItem= NewsItem()
    var cachedList= mutableListOf<NewsItem>()


    //ниже - дзагрузка из 1 источника
    fun downloadFeedsDetails(url:String): Disposable?{
         return when (getFeedsSource(url)) {
            FeedsSource.HABR -> {
               RemoteFactory.getHabrContentObservable(url).map(::parseHabrFeedsDetails).subscribe(::_onSuccess,::_onError)
            }
            FeedsSource.PROGER ->{
               RemoteFactory.getProgerContentObservable(url).map(::parseProgerFeedsDetails).subscribe(::_onSuccess,::_onError)
            }
             FeedsSource.BOTH->{return null}
        }
    }

    fun cleareCache(){
        cachedList= mutableListOf()
        newsStore.postValue(NewsItem())
    }

    fun parseHabrFeedsDetails(hc: HabrContent): NewsItem   {
        return NewsItem(
                title = hc.title,
                content = hc.content,
                date = hc.date,
                picture = hc.image,
                link = hc.link,
                id = hc.id,
                sourceKind = FeedsSource.HABR
        )

    }

    fun parseProgerFeedsDetails(pc: ProgerContent):NewsItem {
      return NewsItem(
                title = pc.title,
                content = pc.content,
                date = pc.date,
                picture =pc.image,
                link = pc.link,
                id = pc.id,
                sourceKind = FeedsSource.PROGER)
    }

    fun _onSuccess(t:NewsItem) {
        cachedList.add(t)
        newsStore.postValue(t)
        isDownloadSuccessful.postValue(true)
    }

    fun _onError(e: Throwable) {
        isDownloadSuccessful.postValue(false)
        e.printStackTrace()
    }
}