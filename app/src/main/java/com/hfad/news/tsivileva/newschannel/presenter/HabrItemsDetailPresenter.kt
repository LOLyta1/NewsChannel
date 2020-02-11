package com.hfad.news.tsivileva.newschannel.presenter

import android.util.Log

import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.HabrItemsInfo
import com.hfad.news.tsivileva.newschannel.network.INetwork
import com.hfad.news.tsivileva.newschannel.network.NetworkClientHabrDetails
import com.hfad.news.tsivileva.newschannel.view.IView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


class HabrItemsDetailPresenter(val view: IView,var url:String?) : IPresenter {

var subscription: DisposableObserver<HabrItemsInfo>?=null

    override fun getNews(isUpdate: Boolean) {
         subscription= createObservable()?.subscribeWith(createObserver())
    }

    private fun createObservable(): Observable<HabrItemsInfo>? {
        Log.d("mylog","HabrItemsDetailPresenter.createObservable() - URL :  ${url}")

        return NetworkClientHabrDetails().
                instance(url).
                create(INetwork::class.java).
                loadHabrDetails()?.
                subscribeOn(Schedulers.io())?.
                observeOn(AndroidSchedulers.mainThread())
    }

    private fun createObserver(): DisposableObserver<HabrItemsInfo> {
        return object : DisposableObserver<HabrItemsInfo>() {
            override fun onComplete() {
                view.showComplete()
                dispose()
            }
            override fun onNext(t: HabrItemsInfo) {
               // view.showNews(NewsItem(title = t.title,summarry = t.content,date = t.date))
                Log.d("mylog", "HabrItemDetailPresenter. onNext() , t.content="+t.content)

            }
            override fun onError(e: Throwable) {
                view.showError(e)
                e.printStackTrace()
            }
        }
    }
}