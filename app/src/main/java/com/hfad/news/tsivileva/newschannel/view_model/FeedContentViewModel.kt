package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.model.local.Favorite
import com.hfad.news.tsivileva.newschannel.model.local.Content
import com.hfad.news.tsivileva.newschannel.repository.local.NewsDatabase
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FeedContentViewModel(val app: Application) : AndroidViewModel(app) {
    private var serverSubscription: Disposable? = null
    private var newsDescriptionId: Long? = null

    var newsLiveData = MutableLiveData<DownloadingState<Content>>()

    fun downloadContent(url: String?, newsDescriptionId: Long?) {
        if (newsDescriptionId != null && url != null) {
            this.newsDescriptionId = newsDescriptionId

            if (getSourceByLink(url) == FeedsSource.PROGER) {
                serverSubscription = RemoteRepository
                        .getProgerContentObservable(url)

                        .subscribeOn(Schedulers.io())
                        .subscribe(::_onSuccess, ::_onError)
            } else
                if (getSourceByLink(url) == FeedsSource.HABR) {
                    serverSubscription = RemoteRepository
                            .getHabrContentObservable(url)

                            .subscribeOn(Schedulers.io())
                            .subscribe(::_onSuccess, ::_onError)
                }
        }
    }

    private fun _onSuccess(t: Content) {
        val _news = Content(
                descriptionId = newsDescriptionId,
                contentText = t.contentText,
                id = null
        )
        NewsDatabase.instance(getApplication())?.getApi()?.insertContent(_news)
        newsLiveData.postValue(DownloadingSuccessful(t))
    }

    private fun _onError(e: Throwable) {
        if (newsDescriptionId != null) {
            val cachedContent = Content(id = null, descriptionId = newsDescriptionId, contentText = "")
                    NewsDatabase.instance(getApplication())?.getApi()?.selectContentByDescriptionId(newsDescriptionId!!)
                    ?.let {
                        Content(id = null, descriptionId = newsDescriptionId, contentText = it)
                    }
            newsLiveData.postValue(DownloadingError(e, cachedContent))
            Log.d(DEBUG_LOG,"FeedContentViewModel._onError -content:${cachedContent.contentText}")
        }
    }

    override fun onCleared() {
        serverSubscription?.dispose()
        NewsDatabase.destroyInstance()
        super.onCleared()
    }



    fun removeFromFavorite(id: Long?) {
        if(id!=null){
            val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
            databaseApi
                    ?.insertIntoFav(fav = Favorite(null, id, false))
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribe()
        }
    }

    fun addToFavorite(id: Long?) {
        if(id!=null){
            val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
            databaseApi
                    ?.insertIntoFav(fav = Favorite(null, id, true))
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribe()
        }
    }
}