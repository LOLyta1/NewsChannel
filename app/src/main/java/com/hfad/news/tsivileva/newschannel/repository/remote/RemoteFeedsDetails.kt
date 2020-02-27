package com.hfad.news.tsivileva.newschannel.repository.remote

import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.FeedsContentSource
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.getFeedsSource
import com.hfad.news.tsivileva.newschannel.logIt
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.disposables.Disposable

class RemoteFeedsDetails {

  //  var newsStore = MutableLiveData<NewsItem>()

    val isDownloadSuccessful = MutableLiveData<Boolean>()
    var contentItem=MutableLiveData<NewsItem>()
   val disposable:Disposable?=null
    //ниже - дзагрузка из 1 источника
    fun downloadFeedsDetails(url:String, source: FeedsContentSource): Disposable{
        logIt("RemoteFeedsDetails","downloadFeedsDetails"," ссылка - $url")
         return when (source) {
             FeedsContentSource.HABR -> {
                 logIt("RemoteFeedsDetails","downloadFeedsDetails"," HABR")
               RemoteFactory.getHabrContentObservable(url).map(::parseHabrFeedsDetails).subscribe(::_onSuccess,::_onError)
            }
             FeedsContentSource.PROGER ->{
                 logIt("RemoteFeedsDetails","downloadFeedsDetails"," PROGER")

                 RemoteFactory.getProgerContentObservable(url).map(::parseProgerFeedsDetails).subscribe(::_onSuccess,::_onError)
            }
        }
    }

    fun _onSuccess(t:NewsItem) {
        logIt("RemoteFeedsDetails","_onSuccess"," -  загружено - ${t}")
        contentItem.postValue(t)
        isDownloadSuccessful.postValue(true)
    }

    fun _onError(e: Throwable) {
        isDownloadSuccessful.postValue(false)
        logIt("RemoteFeedsDetails","_onError"," -   ${e.message}")
        e.printStackTrace()
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

}