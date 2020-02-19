package com.hfad.news.tsivileva.newschannel.repository.remote

import androidx.lifecycle.MutableLiveData
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.Sources
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
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
        private var subscriptionLiveData = MutableLiveData<DisposableObserver<MutableList<List<Any>?>>>()
        private val cacheList = mutableListOf<NewsItem>()

        val newsLiveData = MutableLiveData(cacheList)
        val loadSuccessfulLiveData = MutableLiveData<Boolean>()

        fun load() {
            if (cacheList.isEmpty()) {
                val subscription = createObservable().subscribeWith(createObserver())
                subscriptionLiveData.postValue(subscription)
            } else {
                newsLiveData.postValue(cacheList)
                loadSuccessfulLiveData.postValue(true)
            }
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
                            newsItem.sourceKind = Sources.HABR
                            newsItem.id = getIdInLink(it.link)
                            newsItem.link = it.link
                            newsItem.picture = it.image
                            newsItem.title = it.title
                            newsItem.date = it.date
                            cacheList.add(newsItem)
                            newsLiveData.postValue(cacheList)
                        }

                        is Proger.Channel.Item -> {
                            var newsItem = NewsItem()
                            newsItem.sourceKind = Sources.PROGER
                            newsItem.id = getIdInLink(it.link)
                            newsItem.link = it.link
                            newsItem.title = it.title
                            newsItem.date = it.pubDate
                            newsItem.picture = "https://tproger.ru/apple-touch-icon.png"
                            cacheList.add(newsItem)
                            newsLiveData.postValue(cacheList)
                        }
                    }
                }
            }
        }
    }


    class NewsContent() {

        val loadingSuccessful = MutableLiveData<Boolean>()
        val cachedList = mutableListOf<NewsItem>()
        val cachedNewsItem = MutableLiveData<NewsItem>()
        val loadedNewsItem = MutableLiveData<NewsItem>()

        private var subscriptionHabr = MutableLiveData<Disposable>()
        private var subscriptionProger = MutableLiveData<Disposable>()

        fun loadHabr(url: String) {
            val id = getIdInLink(url)
            val newsItem = cachedList.find { it.id == id && it.sourceKind == Sources.HABR }
            if (newsItem == null) {
                createObservableHabrItem(url).subscribe(createObserverHabr())
            } else {
                cachedNewsItem.postValue(newsItem)
            }
        }

        fun loadProger(url: String) {
            val id = getIdInLink(url)
            val newsItem = cachedList.find { it.id == id && it.sourceKind == Sources.PROGER }
            if (newsItem == null) {
                createObservableProgerItem(url).subscribe(createObserverProger())
            } else {
                cachedNewsItem.postValue(newsItem)
            }

        }

        fun stopLoadHabr() {
            subscriptionHabr.value?.dispose()
        }

        fun stopLoadProger() {
            subscriptionProger.value?.dispose()
        }

        fun cleareContent() {
            cachedNewsItem.value = NewsItem()
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
                    loadedNewsItem.postValue(newsItem)
                    cachedList.add(newsItem)
                }

                override fun onSubscribe(d: Disposable) = subscriptionHabr.postValue(d)
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
                            sourceKind = Sources.PROGER)
                    loadingSuccessful.postValue(true)
                    loadedNewsItem.postValue(newsItem)
                    cachedList.add(newsItem)
                }
                override fun onSubscribe(d: Disposable) = subscriptionProger.postValue(d)
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

