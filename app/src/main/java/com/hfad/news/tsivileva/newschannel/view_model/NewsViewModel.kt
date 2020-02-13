package com.hfad.news.tsivileva.newschannel.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrItemsInfo
import com.hfad.news.tsivileva.newschannel.model.tproger.TProger

import com.hfad.news.tsivileva.newschannel.repository.remote.*
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

class NewsViewModel : ViewModel() {
    var newsArray = mutableListOf<NewsItem>()
    var newsLiveData = MutableLiveData(newsArray)
    var subscription = MutableLiveData<DisposableObserver<MutableList<List<Any>?>>>()

    var newsLoaded = MutableLiveData<Boolean>()
    var habrLoaded = MutableLiveData<Boolean>()

    var error = MutableLiveData<String>()


    fun addNews(item: NewsItem) {
        newsArray.add(item)
        newsLiveData.postValue(newsArray)
    }

    fun cleareNews(){
        newsArray.clear()
        newsLiveData.value=newsArray
    }

    fun updateHabrNews(index:Int, item:NewsItem?){
        if(item!=null){
            newsArray[index]=item
            newsLiveData.postValue(newsArray)
        }
    }

    fun stopSubscription() {
        subscription.value?.let {
            it.dispose()
            subscription = MutableLiveData(it)
        }
    }

    fun loadNews() {
            val disposableObserver = object : DisposableObserver<MutableList<List<Any>?>>() {
                override fun onComplete() = newsLoaded.postValue(true)
                override fun onNext(t: MutableList<List<Any>?>) = parsNext(t)
                override fun onError(e: Throwable) = error.postValue("ошибка при загрузке данных ${e.message}")
            }
            val observable = RemoteRepository().createObservableAllNews()
            val subscription = observable.subscribeWith(disposableObserver)
            this.subscription.postValue(subscription)
    }




    fun loadHabrNewsDetails(item: NewsItem?) {

           var observer=object : SingleObserver<HabrItemsInfo> {
               override fun onError(e: Throwable) {
                   Log.d("my_log","loadHabrNewsInfo() - onError : ${e.message}")
                   e.printStackTrace()
               }
               override fun onSuccess(t: HabrItemsInfo) {
                  val index=newsArray.indexOf(item)
                  item?.content=t.content
                  updateHabrNews(index,item)
               }
               override fun onSubscribe(d: Disposable) {
               }
           }
         item?.link?.let {
             RemoteRepository().createObservableHabrItem(it).subscribeWith(observer)
         }
   }



    private fun parsNext(list: MutableList<List<Any>?>) {
        list.forEach { list1 ->
            list1?.forEach {
                when (it) {
                    is Habr.HabrlItems -> {
                        var newsItem = NewsItem()
                        Log.d("mylog", "onNext - habr")
                        newsItem.link = it.link
                        newsItem.picture = it.habrItemsDetail?.imageSrc
                        newsItem.title = it.title
                        newsItem.date = it.date
                        addNews(newsItem)

                    }
                    is TProger.Channel.Item -> {
                        var newsItem = NewsItem()
                        Log.d("mylog", "onNext - proger")
                        newsItem.link = it.link
                        newsItem.title = it.title
                        newsItem.date = it.pubDate
                        newsItem.picture = "https://pbs.twimg.com/profile_images/857551974442651648/D5cZLXTf.jpg"
                        addNews(newsItem)
                    }
                }
            }
        }
    }
}