package com.hfad.news.tsivileva.newschannel.repository.remote

import android.database.Observable
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.FeedsContentSource
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.parseHabrFeedsContent
import com.hfad.news.tsivileva.newschannel.parseProgerFeedsContent
import com.hfad.news.tsivileva.newschannel.printId
import com.hfad.news.tsivileva.newschannel.repository.DownloadingError
import com.hfad.news.tsivileva.newschannel.repository.DownloadingProgress
import com.hfad.news.tsivileva.newschannel.repository.DownloadingState
import com.hfad.news.tsivileva.newschannel.repository.DownloadingSuccessful
import io.reactivex.disposables.Disposable

class FeedContentRepository {
    var isDownloadSuccessful = MutableLiveData<DownloadingState>()
    var disposable: Disposable? = null
    var newsCache = mutableListOf<NewsItem>()
    var newsItem = NewsItem()



    fun _onNext(t: NewsItem) {
        newsItem=t
        newsCache.add(t)
        isDownloadSuccessful.postValue(DownloadingProgress(""))
        printId(newsCache)
    }

    fun _onError(e: Throwable) {
        isDownloadSuccessful.postValue(DownloadingError(e))
    }

    fun _onComplete() {
        isDownloadSuccessful.postValue(DownloadingSuccessful(newsItem))
        disposable?.dispose()
    }

    fun stopLoad() {
        disposable?.dispose()
    }


}