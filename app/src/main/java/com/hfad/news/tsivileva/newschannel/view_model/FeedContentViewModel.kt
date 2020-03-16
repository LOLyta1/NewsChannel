package com.hfad.news.tsivileva.newschannel.view_model

import android.annotation.SuppressLint
import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.model.local.Content
import com.hfad.news.tsivileva.newschannel.model.local.Favorite
import com.hfad.news.tsivileva.newschannel.repository.local.NewsDatabase
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.security.cert.CertPath

data class ImageDownloading(
        var path: String,
        var progress:Int
){

}
class FeedContentViewModel(val app: Application) : AndroidViewModel(app) {
    private var contentSubscription: Disposable? = null
    private var newsDescriptionId: Long? = null

    var newsLiveData = MutableLiveData<DownloadingState<Content>>()

    var downloadingFileLiveData=MutableLiveData<ImageDownloading>()


    fun downloadContent(url: String?, newsDescriptionId: Long?) {
        if (newsDescriptionId != null && url != null) {
            this.newsDescriptionId = newsDescriptionId

            if (getSourceByLink(url) == FeedsSource.PROGER) {
                contentSubscription = RemoteRepository
                        .getProgerContentObservable(url)

                        .subscribeOn(Schedulers.io())
                        .subscribe(::_onSuccess, ::_onError)
            } else
                if (getSourceByLink(url) == FeedsSource.HABR) {
                    contentSubscription = RemoteRepository
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
            Log.d(DEBUG_LOG, "FeedContentViewModel._onError -content:${cachedContent.contentText}")
        }
    }

    override fun onCleared() {
        contentSubscription?.dispose()
        NewsDatabase.destroyInstance()
        super.onCleared()
    }


    fun removeFromFavorite(id: Long?) {
        if (id != null) {
            val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
            databaseApi
                    ?.insertIntoFav(fav = Favorite(null, id, false))
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribe()
        }
    }

    fun addToFavorite(id: Long?) {
        if (id != null) {
            val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
            databaseApi
                    ?.insertIntoFav(fav = Favorite(null, id, true))
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribe()
        }
    }


    @SuppressLint("CheckResult")
    fun downloadFile(url: String, fileName: String): MutableLiveData<ImageDownloading> {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            val path = "${getApplication<Application>().externalMediaDirs?.get(0)}/${fileName}"

            Log.d(DEBUG_LOG, "путь файла - $path")
            RemoteRepository.getFileDownloadingObservable(filePath = path, url = url)
                    ?.subscribe(
                            { progress ->
                                downloadingFileLiveData.postValue(ImageDownloading(path,progress))
                                Log.d(DEBUG_LOG, "progress ${progress}")
                            },
                            { e ->downloadingFileLiveData.postValue(ImageDownloading(path,-1))
                                Log.d(DEBUG_LOG, "ошибка ${e.message}")
                                e.printStackTrace()
                            },
                            { Log.d(DEBUG_LOG, "Загрузка завершена") })
        }
    return downloadingFileLiveData
    }

}