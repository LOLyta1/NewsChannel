package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.getIdInLink
import com.hfad.news.tsivileva.newschannel.getSourceByLink
import com.hfad.news.tsivileva.newschannel.repository.DownloadedFeed
import com.hfad.news.tsivileva.newschannel.repository.DownloadingError
import com.hfad.news.tsivileva.newschannel.repository.DownloadingProgress
import com.hfad.news.tsivileva.newschannel.repository.DownloadingState
import com.hfad.news.tsivileva.newschannel.repository.local.LocalDatabase
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FeedContentViewModel(val app: Application) : AndroidViewModel(app) {
    val downloading = MutableLiveData<DownloadingState>()
    private var disposable: Disposable? = null
    private var database: LocalDatabase? = LocalDatabase.instance(getApplication())
    private var link: String ?=null

    fun download(url: String) {
        link = url

                loadFromSource(url)
                Log.d(DEBUG_LOG,"загрузка из инета")


    }
    fun loadFromSource(url:String){
        if (getSourceByLink(url) == FeedsSource.PROGER) {
            disposable = RemoteRepository.getProgerContentObservable(url)
                    .subscribe(::_onSuccess, ::_onError)
        } else {
            if (getSourceByLink(url) == FeedsSource.HABR)
                disposable = RemoteRepository.getHabrContentObservable(url)
                        .subscribe(::_onSuccess, ::_onError)
        }
    }


    fun _onSuccess(t: NewsItem) {
        Log.d(DEBUG_LOG,"_onSuccess - Инстанс базы ${database}")
        if(link!=null){

           database?.getLocalRepo()?.update(t)
            val nserted=database?.getLocalRepo()?.selectById(t.id)
            downloading.postValue(DownloadedFeed(nserted))
        }

//        database?.getLocalRepo()?.insert(t)
//        database
//                ?.getLocalRepo()
//                ?.selectByLink(t.link)
//                ?.doOnSuccess {
//                    downloading.postValue(DownloadedFeed(it))
//                    disposable?.dispose()
//                }
//                ?.doOnError { e -> e.printStackTrace() }
//                ?.subscribe()

    }

    fun _onError(e: Throwable) {
        Log.d(DEBUG_LOG,"_onError - Инстанс базы ${database}")
        downloading.postValue(DownloadingError(e))
        link?.let {
            val news= database
                    ?.getLocalRepo()
                    ?.selectByLink(it)

            Log.d(DEBUG_LOG, "FeedContentViewModel - _onError()  - ${news?.date}.${news?.title},content - ${news?.content}")
            downloading.postValue(DownloadedFeed(news))

        }


           /*     ?.doOnSuccess {
                    Log.d(DEBUG_LOG,"вставленное содержимое ${it.link}")
                    downloading.postValue(DownloadedFeed(it))
                    disposable?.dispose()

                }?.subscribe()*/

        e.printStackTrace()

    }


    override fun onCleared() {
        LocalDatabase.destroyInstance()
        super.onCleared()

        disposable?.dispose()
    }

    fun refreshData() {
        downloading.postValue(DownloadedFeed(NewsItem()))
        disposable?.dispose()
        //  link = ""
    }
}