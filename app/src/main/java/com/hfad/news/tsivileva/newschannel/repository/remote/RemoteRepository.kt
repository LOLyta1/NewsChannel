package com.hfad.news.tsivileva.newschannel.repository.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.Sources

import com.hfad.news.tsivileva.newschannel.findNews
import com.hfad.news.tsivileva.newschannel.getIdInLink
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import com.hfad.news.tsivileva.newschannel.printCachedMutableList
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

enum class RemoteRepositoryTypes {
    SIMPLE_XML,
    JSPOON
}


class RemoteRepository {
    companion object {
        fun createRetrofit(baseUrl: String, type: RemoteRepositoryTypes): Retrofit {
            val factory: Converter.Factory = when (type) {
                RemoteRepositoryTypes.SIMPLE_XML -> SimpleXmlConverterFactory.create()
                RemoteRepositoryTypes.JSPOON -> JspoonConverterFactory.create()
            }
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(factory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(OkHttpClient.Builder().build())
                    .build()
        }
    }


    class AllNews() {
        private val HABR_URL = "https://habr.com/ru/rss/all/"
        private val PROGER_URL = "https://tproger.ru/feed/"

        private val newsArray = mutableListOf<NewsItem>()
        val newsLiveData = MutableLiveData(newsArray)

        val loadSuccessfulLiveData = MutableLiveData<Boolean>()

        private var subscriptionLiveData = MutableLiveData<DisposableObserver<MutableList<List<Any>?>>>()

        fun load() {
            val subscription = createObservable().subscribeWith(createObserver())
            subscriptionLiveData.postValue(subscription)
        }

        fun stopLoad() {
            subscriptionLiveData.value?.dispose()
        }

        private fun createObservable(): Observable<MutableList<List<Any>?>> {
            val habrObservable = createRetrofit(HABR_URL, RemoteRepositoryTypes.SIMPLE_XML)
                    .create(IRemoteApi::class.java)
                    .loadHabr()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread());

            val progerObservable = createRetrofit(PROGER_URL, RemoteRepositoryTypes.SIMPLE_XML)
                    .create(IRemoteApi::class.java)
                    .loadProger()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())

            return Observable.zip(habrObservable, progerObservable, BiFunction<Habr, Proger, MutableList<List<Any>?>> { h, p ->
                mutableListOf(h.items, p.channel?.items)
            })
        }

        private fun createObserver(): DisposableObserver<MutableList<List<Any>?>> {
            return object : DisposableObserver<MutableList<List<Any>?>>() {
                override fun onComplete() = loadSuccessfulLiveData.postValue(true)
                override fun onNext(t: MutableList<List<Any>?>) = parseNews(t)
                override fun onError(e: Throwable) = loadSuccessfulLiveData.postValue(false)
            }
        }

        private fun parseNews(list: MutableList<List<Any>?>) {
            var index = 0L
            list.forEach { list1 ->
                list1?.forEach {
                    index++
                    when (it) {
                        is Habr.HabrlItems -> {
                            var newsItem = NewsItem()
                            newsItem.link = it.link
                            newsItem.picture = it.image
                            Log.d(DEBUG_LOG,"ссылка на картинку хабра - ${newsItem.picture}")
                            newsItem.title = it.title
                            newsItem.date = it.date
                            newsArray.add(newsItem)
                            newsLiveData.postValue(newsArray)
                        }

                        is Proger.Channel.Item -> {
                            var newsItem = NewsItem()
                            newsItem.link = it.link
                            newsItem.title = it.title
                            newsItem.date = it.pubDate
                            newsItem.picture = "https://tproger.ru/apple-touch-icon.png"
                            newsArray.add(newsItem)
                            newsLiveData.postValue(newsArray)
                        }
                    }
                }
            }
        }
    }

    class NewsContent() {
        val contentLiveData = MutableLiveData(NewsItem())
        val loadingSuccessful = MutableLiveData<Boolean>()
        val cachedList = mutableListOf<NewsItem>()

        private var subscriptionHabr = MutableLiveData<Disposable>()
        private var subscriptionProger = MutableLiveData<Disposable>()

        fun loadHabr(url: String) {
            createObservableHabrItem(url).subscribe(createObserverHabr())
        }

        fun loadProger(url: String) {
            createObservableProgerItem(url).subscribe(createObserverProger())
        }

        fun stopLoadHabr() {
            subscriptionHabr.value?.dispose()
        }

        fun stopLoadProger() {
            subscriptionProger.value?.dispose()
        }

        fun cleareContent(){
            contentLiveData.value= NewsItem()
        }

        private fun createObserverHabr(): SingleObserver<HabrContent> {
            return object : SingleObserver<HabrContent> {
                override fun onSuccess(t: HabrContent) {
                    val newsItem = NewsItem(
                            title = t.title,
                            content = t.content,
                            date = t.date,
                            picture = t.image,
                            link = t.link,
                            id = getIdInLink(t.link),
                            sourceKind = Sources.HABR)
                    loadingSuccessful.postValue(true)
                    contentLiveData.postValue(newsItem)

                    if (cachedList.find { it.link == newsItem.link } == null) {
                        cachedList.add(newsItem)
                    }
                    printCachedMutableList("NewsContent", "createObserverHabr()", cachedList)
                }

                override fun onSubscribe(d: Disposable) {
                    subscriptionHabr.postValue(d)
                }

                override fun onError(e: Throwable) = loadingSuccessful.postValue(false)
            }
        }

        private fun createObserverProger(): SingleObserver<ProgerContent> {
            return object : SingleObserver<ProgerContent> {
                override fun onSuccess(t: ProgerContent) {
                    val newsItem = NewsItem(
                            title = t.title,
                            content = t.content,
                            date = t.date,
                            picture = t.image,
                            link = t.link,
                            id = getIdInLink(t.link),
                            sourceKind = Sources.Proger)
                    loadingSuccessful.postValue(true)
                    contentLiveData.postValue(newsItem)
                    if (!findNews(cachedList, newsItem)) {
                        cachedList.add(newsItem)
                    }
                    printCachedMutableList("NewsContent", "createObserverProger()", cachedList)
                }

                override fun onSubscribe(d: Disposable) {
                    subscriptionProger.postValue(d)
                }

                override fun onError(e: Throwable) = loadingSuccessful.postValue(false)
            }
        }

        private fun createObservableHabrItem(url: String): Single<HabrContent> {
            return createRetrofit(url, RemoteRepositoryTypes.JSPOON)
                    .create(IRemoteApi::class.java)
                    .loadHabrDetails()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
        }

        private fun createObservableProgerItem(url: String): Single<ProgerContent> {
            return createRetrofit(url, RemoteRepositoryTypes.JSPOON)
                    .create(IRemoteApi::class.java)
                    .loadProgDetails()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
        }
    }
}

