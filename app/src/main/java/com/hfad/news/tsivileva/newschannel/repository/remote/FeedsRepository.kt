package com.hfad.news.tsivileva.newschannel.repository.remote


import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem

import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.view_model.Sort
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction


class FeedsRepository {
    fun getObservableFeeds() : Observable<MutableList<NewsItem>>{
        return Observable.zip(
                        RemoteFactory.getHabrObservable(),
                        RemoteFactory.getProgerObservable(),
                        BiFunction<Habr, Proger, MutableList<List<Any>?>> { h, p ->
                            mutableListOf(h.items, p.channel?.items)
                        }).map(::parseFeed)
   }


}