package com.hfad.news.tsivileva.newschannel.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr

import com.hfad.news.tsivileva.newschannel.model.tproger.Item
import com.hfad.news.tsivileva.newschannel.model.tproger.TProger
import com.hfad.news.tsivileva.newschannel.network.INetwork
import com.hfad.news.tsivileva.newschannel.network.NetworkClientHabr
import com.hfad.news.tsivileva.newschannel.network.NetworkClientProger
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver

class SharedViewModel : ViewModel() {

    lateinit var subscription:DisposableObserver<Any>
    private var newsArray = mutableListOf<NewsItem>()
    val newsMutableLiveData = MutableLiveData(newsArray)


    fun addNews(habrItem: NewsItem) {
        newsArray.add(habrItem)
        newsMutableLiveData.postValue( newsArray)
    }


    fun getAllNews() {
        val habrObservable: Observable<Habr> = NetworkClientHabr.getRetrofit().create(INetwork::class.java).loadNews()
        val progerObservable: Observable<TProger> = NetworkClientProger.getRetrofit().create(INetwork::class.java).loadTProger()

        val observable:Observable<Any> = Observable.merge(habrObservable,progerObservable)
        val observer=object:DisposableObserver<Any>(){

            override fun onComplete() {
               Log.d("mylog","onComplete")
            }

            override fun onNext(t: Any) {

                when(t) {
                     is TProger-> {
                         t.channel.item.forEach{
                             var newsItem=NewsItem()
                             newsItem.date=it.pubDate
                             newsItem.picture="https://pbs.twimg.com/profile_images/857551974442651648/D5cZLXTf.jpg";
                             newsItem.summarry=it.description
                             newsItem.title=it.title
                             newsItem.link=it.link
                             Log.d("mylog","onNext-habr")
                             addNews(newsItem)
                         }


                     }
                     is Habr->{
                         t.habrlItems?.forEach {
                             var newsItem=NewsItem()
                             newsItem.date=it.date
                             newsItem.link=it.link
                             newsItem.title=it.title
                             newsItem.summarry=it.habrItemsDetail?.description
                             newsItem.picture=it.habrItemsDetail?.imageSrc
                             Log.d("mylog","onNext-Habr, image = ${newsItem.picture}, description=${newsItem.summarry} ")

                             addNews(newsItem)
                         }

                     }
                }

            }

            override fun onError(e: Throwable) {
                Log.d("mylog","error ${e.message}")
            }

        }

      subscription= observable.subscribeWith(observer)
    }


}