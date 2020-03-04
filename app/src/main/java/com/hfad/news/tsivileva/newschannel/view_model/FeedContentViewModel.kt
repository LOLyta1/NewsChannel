package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.getSourceByLink
import com.hfad.news.tsivileva.newschannel.repository.local.LocalDatabase
import com.hfad.news.tsivileva.newschannel.repository.local.NewsAndContent
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FeedContentViewModel(val app: Application) : AndroidViewModel(app) {
    var news: LiveData<NewsAndContent>? = null

    private var disposable: Disposable? = null
    private var database: LocalDatabase? = LocalDatabase.instance(getApplication())
    private var link: String? = null

    fun download() {
        return
    }

    fun downloadingFromInternet(url: String, id:Long){
        news=RemoteRepository.downloadingFromInternet(database,url,id)
    }

    fun downloadFromDatabase(id: Long): LiveData<NewsAndContent>? {
        return database?.getLocalRepo()?.selectNewsAndContent(id)
    }


//       disposable= database?.getLocalRepo()?.selectByLink(url)?.subscribeOn(Schedulers.io())
//               ?.doOnSuccess {
//           Log.d(DEBUG_LOG, "контект нужно обновить в ${it.id}, content ${it.content}")
//            if(it!=null && it.content.isEmpty()){
//                loadFromNetwork(url)
//            }else{
//                Log.d(DEBUG_LOG, "загрузка c базы")
//                downloading.postValue(DownloadedFeed(it))
//            }
//        }?.doOnError{
//           it.printStackTrace()
//       }?.doFinally {
//            disposable?.dispose()
//        }?.subscribe()


/*

fun loadFromNetwork(url: String): LiveData<NewsAndContent> {
    Log.d(DEBUG_LOG, "загрузка из инета")

}

fun _onSuccess(t: News) {
//        Log.d(DEBUG_LOG, "_onSuccess - контент для вставки ${t.content}")
//     //        val id= database?.getLocalRepo()?.insert(t)
//        Log.d(DEBUG_LOG, "_onSuccess - вставлена запись с id ${id}")
//        downloading.postValue(DownloadedFeed(t))
}

fun _onError(e: Throwable) {
    Log.d(DEBUG_LOG, "_onError - Инстанс базы ${database}")
    //       downloading.postValue(DownloadingError(e))
}
*/


/*
override fun onCleared() {
    disposable?.dispose()
    LocalDatabase.destroyInstance()
    super.onCleared()

}
*/

/*fun refreshData() {
    //downloading.postValue(DownloadedFeed(News()))
    disposable?.dispose()
    //  link = ""
}*/
}