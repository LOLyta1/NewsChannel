package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.Preference
import com.hfad.news.tsivileva.newschannel.SortType
import com.hfad.news.tsivileva.newschannel.model.local.Favorite
import com.hfad.news.tsivileva.newschannel.model.local.NewsAndFav
import com.hfad.news.tsivileva.newschannel.model.local.NewsDescription
import com.hfad.news.tsivileva.newschannel.repository.local.NewsDatabase
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class FeedViewModel(val app: Application) : AndroidViewModel(app) {

    private var subscription: Disposable? = null
    var downloading = MutableLiveData<List<NewsAndFav>>()


    fun downloadFeeds() {
        subscription = RemoteRepository
                .getFeedsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(::onNext, ::onError, ::onComplete)
    }

    private fun onComplete() {
        val api = NewsDatabase.instance(getApplication())?.getApi()
        downloading.postValue(api?.selectAllDescriptionAndFav())
    }

    private fun onError(e: Throwable) {
        val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
        downloading.postValue(databaseApi?.selectAllDescriptionAndFav())
    }

    private fun onNext(item: List<NewsDescription>) {
        NewsDatabase.instance(getApplication())?.getApi()?.insertIntoDescription(item)
    }

    override fun onCleared() {
        NewsDatabase.destroyInstance()
        subscription?.dispose()
        super.onCleared()
    }

    fun removeFromFavorite(id: Long?) {
        if(id!=null){
            Thread(Runnable {
                val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
                databaseApi?.insertIntoFav(fav = Favorite(null, id, false))
                downloading.postValue(databaseApi?.selectAllDescriptionAndFav())
            }).start()
        }

    }

    fun addToFavorite(id: Long?) {
        if(id!=null){
            Thread(Runnable {
                val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
                databaseApi?.insertIntoFav(fav = Favorite(null, id, true))
                downloading.postValue(databaseApi?.selectAllDescriptionAndFav())
            }).start()
        }
    }

}