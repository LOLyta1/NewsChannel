package com.hfad.news.tsivileva.newschannel.view_model

import android.app.Application

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
    private var filters:Filters?=Filters()
    private var subscription: Disposable? = null
    var downloading = MutableLiveData<List<NewsAndFav>>()



    fun downloadFeeds(filters: Filters?) {
        this.filters=filters
        subscription = RemoteRepository
                .getFeedsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(::onNext, ::onError, ::onComplete)
    }


    private fun onComplete() {
        load()
    }

    private fun onError(e: Throwable) {
        load()
    }

    private fun onNext(item: List<NewsDescription>) {
        NewsDatabase.instance(getApplication())?.getApi()?.insertIntoDescription(item)
    }



    override fun onCleared() {
        NewsDatabase.destroyInstance()
        subscription?.dispose()
        super.onCleared()
    }

    fun removeFromFavorite(id: Long?, filters: Filters?) {
        this.filters=filters
        if(id!=null){
            val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
            databaseApi
                    ?.insertIntoFav(fav = Favorite(null, id, false))
                    ?.subscribeOn(Schedulers.io())
                    ?.doOnSuccess { load() }
                    ?.subscribe()
        }

    }

    fun addToFavorite(id: Long?, filters: Filters?) {
        this.filters=filters
        if(id!=null){
                val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
                databaseApi
                        ?.insertIntoFav(fav = Favorite(null, id, true))
                        ?.subscribeOn(Schedulers.io())
                        ?.doOnSuccess {load()}
                        ?.subscribe()
        }
    }

    private fun load(){
        filters?.let { _preference ->
            val databaseApi = NewsDatabase.instance(getApplication())?.getApi()
            var list=databaseApi?.selectAllDescriptionAndFav()

            if(_preference.showOnlyFav){
                list=list?.filter { it.newsFav?.isFav==true }
            }
            when(_preference.source){
                FeedsSource.HABR ->  list=list?.filter { it.newsInfo.sourceKind==FeedsSource.HABR }
                FeedsSource.PROGER -> list=list?.filter { it.newsInfo.sourceKind==FeedsSource.PROGER }
                FeedsSource.BOTH ->  list=list?.filter {it.newsInfo.sourceKind==FeedsSource.HABR ||  it.newsInfo.sourceKind==FeedsSource.PROGER}
            }
            when(_preference.sortType){
                SortType.ASC -> list=list?.sortedBy { it.newsInfo.date }
                SortType.DESC -> list=list?.sortedByDescending { it.newsInfo.date }
            }
            downloading.postValue(list)
        }
    }

}