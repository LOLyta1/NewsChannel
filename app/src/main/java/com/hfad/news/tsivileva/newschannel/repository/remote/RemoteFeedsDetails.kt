package com.hfad.news.tsivileva.newschannel.repository.remote

import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.FeedsContentSource
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.logIt
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import io.reactivex.disposables.Disposable

class RemoteFeedsDetails {

  //  var newsStore = MutableLiveData<NewsItem>()

    val isDownloadSuccessful = MutableLiveData<Boolean>()
    var contentItem=MutableLiveData<NewsItem>()
    var disposable:Disposable?=null
    //ниже - дзагрузка из 1 источника
    fun downloadFeedsDetails(url:String, source: FeedsContentSource){
        logIt("RemoteFeedsDetails","downloadFeedsDetails"," ссылка - $url")
           when (source) {
             FeedsContentSource.HABR -> {
                 logIt("RemoteFeedsDetails","downloadFeedsDetails"," HABR")
              disposable=RemoteFactory.getHabrContentObservable(url).map(::parseHabrFeedsDetails).subscribe(::_onNext,::_onError, ::_onComplete)
            }
             FeedsContentSource.PROGER ->{
                 logIt("RemoteFeedsDetails","downloadFeedsDetails"," PROGER")
                disposable=RemoteFactory.getProgerContentObservable(url).map(::parseProgerFeedsDetails).subscribe(::_onNext,::_onError, ::_onComplete)
            }
        }
    }

    fun _onNext(t:NewsItem) {
        logIt("RemoteFeedsDetails","_OnNext"," -  загружено - ${t}")
        contentItem.postValue(t)
    }

    fun _onError(e: Throwable) {
        isDownloadSuccessful.postValue(false)
        logIt("RemoteFeedsDetails","_onError"," -   ${e.message}")
        e.printStackTrace()
    }

    fun _onComplete(){
        isDownloadSuccessful.postValue(true)
        logIt("RemoteFeedsDetails","_onComplete","")

    }

    fun stopLoad(){
        disposable?.dispose()
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