package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.model.local.Favorite
import com.hfad.news.tsivileva.newschannel.model.local.NewsAndFav
import com.hfad.news.tsivileva.newschannel.model.local.NewsDescription
import com.hfad.news.tsivileva.newschannel.repository.local.NewsDatabase
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class FeedViewModel(val app: Application) : AndroidViewModel(app) {

    private var subscription: Disposable? = null

    var downloading = MutableLiveData<DownloadingState<List<NewsAndFav>?>>()

    fun downloadFeeds() {
        subscription = RemoteRepository
                .getFeedsObservable()
                .subscribe(::onNext, ::onError, ::onComplete)
    }

    private fun onComplete() {
        val list = NewsDatabase.instance(getApplication())?.getApi()?.selectAllDescriptionAndFav()
        if (list != null) {
            downloading.postValue(DownloadingSuccessful(list))
        }
    }

    private fun onError(e: Throwable) {
        val cachedList = NewsDatabase.instance(getApplication())?.getApi()
                ?.selectAllDescriptionAndFav()

        downloading.postValue(DownloadingError(e, cachedList))

    }

    private fun onNext(item: List<NewsDescription>) {
        NewsDatabase.instance(getApplication())?.getApi()?.insertIntoDescription(item)
    }

    fun showFav() {
        NewsDatabase.instance(getApplication())?.getApi()
                ?.selectDescriptionAndFaw()
                ?.subscribeOn(Schedulers.io())
                ?.doOnSuccess {
                    downloading.postValue(DownloadingSuccessful(it))
                }
                ?.doOnError {
                    downloading.postValue(DownloadingError(it, null))
                }
                ?.subscribe()
    }


    fun sortNews(sortTypeKind: SortType, source: FeedsSource) {
        var list: List<NewsDescription>? = listOf<NewsDescription>()
        when (source) {
            FeedsSource.HABR, FeedsSource.PROGER -> {
                when (sortTypeKind) {
                    SortType.ASC -> list = NewsDatabase.instance(getApplication())?.getApi()?.selectSortedByDateAsc(source)
                    SortType.DESC -> list = NewsDatabase.instance(getApplication())?.getApi()?.selectDescriptionByDateDesc(source)
                }
            }
            FeedsSource.BOTH -> {
                when (sortTypeKind) {
                    SortType.ASC -> list = NewsDatabase.instance(getApplication())?.getApi()?.selectAllSortedByDateAsc()
                    SortType.DESC -> list = NewsDatabase.instance(getApplication())?.getApi()?.selectDescriptionSortedByDateDesc()
                }
            }
        }
      // if(list!=null)  downloading.postValue(DownloadingSuccessful(list) )
    }

    override fun onCleared() {
        NewsDatabase.destroyInstance()
        subscription?.dispose()
        super.onCleared()
    }

    fun searchByTitle(title: String) {
        val list = NewsDatabase.instance(getApplication())?.getApi()?.selectDescriptionByTitle("%$title%")
        //   if(list!=null)  downloading.postValue(DownloadingSuccessful(list) )
    }

    fun removeFromFavorite(id: Long) {
        Log.d(DEBUG_LOG, "FeedViewModel.removeFromFavorite() - $id")
        NewsDatabase.instance(getApplication())?.getApi()?.insertIntoFav(fav = Favorite(null, id, false))?.subscribeOn(Schedulers.io())
                ?.doOnSuccess {
                    downloading.postValue(DownloadingSuccessful(NewsDatabase.instance(getApplication())?.getApi()?.selectAllDescriptionAndFav()))

                    Log.d(DEBUG_LOG, "изменена запись с id - $it , isFav - false")
                }?.subscribe()

    }

    fun addToFavorite(id: Long) {
        NewsDatabase.instance(getApplication())?.getApi()?.insertIntoFav(fav = Favorite(null, id, true))
                ?.subscribeOn(Schedulers.io())
                ?.doOnSuccess {
                    Log.d(DEBUG_LOG, "изменена запись с id - $it , isFav - true")
                      downloading.postValue(DownloadingSuccessful(NewsDatabase.instance(getApplication())?.getApi()?.selectAllDescriptionAndFav()))
                }?.doOnError { e -> e.printStackTrace() }?.subscribe()
        Log.d(DEBUG_LOG, "FeedViewModel.addToFaavorite() - $id")
    }
}