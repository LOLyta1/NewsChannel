package com.hfad.news.tsivileva.newschannel.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.tproger.Item

import com.hfad.news.tsivileva.newschannel.model.tproger.TProger
import com.hfad.news.tsivileva.newschannel.repository.remote.INetwork
import com.hfad.news.tsivileva.newschannel.repository.remote.NetworkClientHabr
import com.hfad.news.tsivileva.newschannel.repository.remote.NetworkClientProger
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver

class SharedViewModel : ViewModel() {

    lateinit var subscription:DisposableObserver<MutableList<List<Any>?>>
    private var newsArray = mutableListOf<NewsItem>()
    val newsMutableLiveData = MutableLiveData(newsArray)


    fun addNews(habrItem: NewsItem) {
        newsArray.add(habrItem)
        newsMutableLiveData.postValue( newsArray)
    }


    fun getAllNews() {
        val habrObservable: Observable<Habr> = NetworkClientHabr.getRetrofit().create(INetwork::class.java).loadNews()
        val progerObservable: Observable<TProger> = NetworkClientProger.getRetrofit().create(INetwork::class.java).loadTProger()

        val observable:Observable<MutableList<List<Any>?>> = Observable.zip(habrObservable,progerObservable, BiFunction<Habr,TProger,MutableList<List<Any>?>> {
            h,p->
            mutableListOf(h.habrlItems,p.channel.item)
        })

        val observer=object:DisposableObserver<MutableList<List<Any>?>>(){

            override fun onComplete() {
               Log.d("mylog","onComplete")
            }

            override fun onNext(t: MutableList<List<Any>?>) {
                        t.forEach {
                            it?.forEach {
                                when(it){
                                   is Habr.HabrlItems-> Log.d("mylog","ONNEXT - habr ${it.link}")
                                   is Item-> Log.d("mylog","ONNEXT - proger  ${it.link}")
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