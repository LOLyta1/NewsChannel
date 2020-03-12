package com.hfad.news.tsivileva.newschannel.model

import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.getIdInLink
import com.hfad.news.tsivileva.newschannel.model.local.NewsContent
import com.hfad.news.tsivileva.newschannel.model.local.NewsDescription
import com.hfad.news.tsivileva.newschannel.model.remote.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.remote.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.remote.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.remote.proger.ProgerContent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ModelConverter {

    fun toNewsDescription(habr: Habr): List<NewsDescription> {
        val list = mutableListOf<NewsDescription>()
        habr.items?.forEach {
            val newsItem = NewsDescription()
            newsItem.sourceKind = FeedsSource.HABR
            newsItem.id = getIdInLink(it.link)
            newsItem.link = it.link
            newsItem.pictureSrc = it.image
            newsItem.title = it.title
            newsItem.date = it.date
            list.add(newsItem)
        }
        return list
    }

    fun toNewsDescription(proger: Proger): MutableList<NewsDescription> {
        val list = mutableListOf<NewsDescription>()
        proger.channel?.items?.forEach {
            val newsItem = NewsDescription()
            newsItem.sourceKind = FeedsSource.PROGER
            newsItem.id = getIdInLink(it.guid)
            newsItem.link = it.link
            newsItem.title = it.title
            newsItem.date = it.date
            newsItem.pictureSrc = "https://tproger.ru/apple-touch-icon.png"
            list.add(newsItem)
        }
        return list
    }

    fun toNewsDesciption(feed: List<List<Any>?>): Observable<MutableList<NewsDescription>> {
        val list = mutableListOf<NewsDescription>()
        feed.forEach {
            it?.forEach {
                when (it) {
                    is Habr.HabrlItems -> {
                        val newsItem = NewsDescription()
                        newsItem.sourceKind = FeedsSource.HABR
                        newsItem.id = getIdInLink(it.link)
                        newsItem.link = it.link
                        newsItem.pictureSrc = it.image
                        newsItem.title = it.title
                        newsItem.date = it.date
                        list.add(newsItem)
                    }
                    is Proger.Channel.Item -> {
                        val newsItem = NewsDescription()
                        newsItem.sourceKind = FeedsSource.PROGER
                        newsItem.id = getIdInLink(it.guid)
                        newsItem.link = it.link
                        newsItem.title = it.title
                        newsItem.date = it.date
                        newsItem.pictureSrc = "https://tproger.ru/apple-touch-icon.png"
                        list.add(newsItem)
                    }
                }
            }

        }
        return Observable.fromArray(list).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
    }

    fun toNewsContent(hc: HabrContent): NewsContent {
        return NewsContent(
                id = null,
                newsId = null,
                content = hc.content
        )
    }

    fun toNewsContent(pc: ProgerContent): NewsContent {
        return NewsContent(
                id = null,
                newsId = pc.id,
                content = pc.content)
    }

}