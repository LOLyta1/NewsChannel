package com.hfad.news.tsivileva.newschannel.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.tproger.TProger

import com.hfad.news.tsivileva.newschannel.repository.remote.*
import io.reactivex.observers.DisposableObserver

class AllNewsViewModel : ViewModel() {
    var newsArray = mutableListOf<NewsItem>()
    var newsMutLiveData = MutableLiveData(newsArray)
    var newSubscription=MutableLiveData<DisposableObserver<MutableList<List<Any>?>>>()
    var newsMessage=MutableLiveData<String>()


    fun addItem(item: NewsItem) {
        newsArray.add(item)
        newsMutLiveData.postValue(newsArray)
    }
    fun addMessage(errorText:String){
        newsMessage.postValue(errorText)
    }

    fun getHabrNews() {
        var disposableObserver=object: DisposableObserver<MutableList<List<Any>?>>(){
            override fun onComplete() {
               addMessage("загрузка завершена")
            }
            override fun onNext(t:MutableList<List<Any>?>) {
                parsNext(t)
            }
            override fun onError(e: Throwable) {
                addMessage("ошибка при загрузке данных ${e.message}")
            }
        }
        val subscription: DisposableObserver<MutableList<List<Any>?>> =
                RemoteRepository().
                createObservableAllNews().
                subscribeWith(disposableObserver)
        newSubscription.postValue(subscription)
    }

    private fun parsNext(list:MutableList<List<Any>?>){

        list.forEach { list1 ->
            list1?.forEach {
               when (it){
                   is Habr.HabrlItems->{
                       var newsItem=NewsItem()
                       Log.d("mylog","onNext - habr")
                       newsItem.link=it.link
                       newsItem.picture=it.habrItemsDetail?.imageSrc
                       newsItem.title=it.title
                       newsItem.date=it.date
                       newsItem.summarry=it.habrItemsDetail?.description
                       addItem(newsItem)
                   }
                   is TProger.Channel.Item->{
                       var newsItem=NewsItem()
                       Log.d("mylog","onNext - proger")
                       newsItem.link=it.link
                       newsItem.summarry=it.description
                       newsItem.title=it.title
                       newsItem.date=it.pubDate
                       newsItem.picture="https://pbs.twimg.com/profile_images/857551974442651648/D5cZLXTf.jpg"
                       addItem(newsItem)
                   }
               }
            }
        }
    }
}