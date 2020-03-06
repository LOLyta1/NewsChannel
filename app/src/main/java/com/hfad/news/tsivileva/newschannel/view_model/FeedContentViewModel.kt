package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*

import com.hfad.news.tsivileva.newschannel.repository.DownloadingError
import com.hfad.news.tsivileva.newschannel.repository.DownloadingState
import com.hfad.news.tsivileva.newschannel.repository.DownloadingSuccessful
import com.hfad.news.tsivileva.newschannel.repository.local.LocalDatabase

import com.hfad.news.tsivileva.newschannel.repository.local.NewsContent

import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FeedContentViewModel(val app: Application) : AndroidViewModel(app) {

    var news =  MutableLiveData<DownloadingState<NewsContent>>()

    private var disposable: Disposable? = null

    private var newsDescriptionId: Long? = null
    private var link: String? = null



    fun downloadingFromInternet(id: Long?, url: String?) {
        if(id!=null && url!=null){
            newsDescriptionId = id
            link = url
            if (getSourceByLink(url) == FeedsSource.PROGER) {
                disposable = RemoteRepository
                        .getProgerContentObservable(url)
                        .subscribeOn(Schedulers.io())
                        .subscribe(::_onSuccess, ::_onError)
            } else
                if (getSourceByLink(url) == FeedsSource.HABR) {
                    disposable = RemoteRepository
                            .getHabrContentObservable(url)
                            .subscribeOn(Schedulers.io())
                            .subscribe(::_onSuccess, ::_onError)
                }
        }

        //news=RemoteRepository.downloadingFromInternet(database,url,id)
    }


    fun _onSuccess(t: NewsContent) {
        Log.d(DEBUG_LOG, "ID конента ${newsDescriptionId}")
        val _news = NewsContent(newsId = newsDescriptionId, content = t.content, id = null)
        val id = LocalDatabase.instance(getApplication())?.getLocalRepo()?.insertContent(_news)
        Log.d(DEBUG_LOG, "_onSuccess - вставлена запись с id ${id}")
        news.postValue(DownloadingSuccessful(t))

    }
    //printNewsAndContentList(mutableListOf(newsAndontent))
    //news.postValue(newsAndontent)



//news.postValue( NewsAndContent(newsDescription,t))


// downloading.postValue(DownloadedFeed(t))


fun _onError(e: Throwable) {
    if(newsDescriptionId!=null) {
        val content = LocalDatabase.instance(getApplication())?.getLocalRepo()
                ?.selectContentByNewsId(newsDescriptionId!!)
                ?.apply {
                    val cachedNewsContent=NewsContent(null, newsDescriptionId, this)
                    news.postValue(DownloadingError(e,cachedNewsContent))
                }
    }
}


override fun onCleared() {
    disposable?.dispose()
    LocalDatabase.destroyInstance()
    super.onCleared()

}

/*fun refreshData() {
    //downloading.postValue(DownloadedFeed(News()))
    disposable?.dispose()
    //  link = ""
}*/
}