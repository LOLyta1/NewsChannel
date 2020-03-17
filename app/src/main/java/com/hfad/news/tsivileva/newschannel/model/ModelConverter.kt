package com.hfad.news.tsivileva.newschannel.model

import com.hfad.news.tsivileva.newschannel.users_classes.FeedsSource
import com.hfad.news.tsivileva.newschannel.getIdInLink
import com.hfad.news.tsivileva.newschannel.model.local.Content
import com.hfad.news.tsivileva.newschannel.model.local.Description
import com.hfad.news.tsivileva.newschannel.model.remote.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.remote.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.remote.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.remote.proger.ProgerContent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ModelConverter {

    fun toNewsDescription(habr: Habr): List<Description> {
        val list = mutableListOf<Description>()
        habr.items?.forEach {
            val newsItem = Description()
            newsItem.sourceKind = FeedsSource.HABR
            newsItem.id = getIdInLink(it.link)
            newsItem.link = it.link
            newsItem.pictureLink = it.image
            newsItem.title = it.title
            newsItem.date = it.date
            list.add(newsItem)
        }
        return list
    }

    fun toNewsDescription(proger: Proger): MutableList<Description> {
        val list = mutableListOf<Description>()
        proger.channel?.items?.forEach {
            val newsItem = Description()
            newsItem.sourceKind = FeedsSource.PROGER
            newsItem.id = getIdInLink(it.guid)
            newsItem.link = it.link
            newsItem.title = it.title
            newsItem.date = it.date
            newsItem.pictureLink = "https://tproger.ru/apple-touch-icon.png"
            list.add(newsItem)
        }
        return list
    }

    fun toNewsDesciption(feed: List<List<Any>?>): Observable<MutableList<Description>> {
        val list = mutableListOf<Description>()
        feed.forEach {
            it?.forEach {
                when (it) {
                    is Habr.HabrlItems -> {
                        val newsItem = Description()
                        newsItem.sourceKind = FeedsSource.HABR
                        newsItem.id = getIdInLink(it.link)
                        newsItem.link = it.link
                        newsItem.pictureLink = it.image
                        newsItem.title = it.title
                        newsItem.date = it.date
                        list.add(newsItem)
                    }
                    is Proger.Channel.Item -> {
                        val newsItem = Description()
                        newsItem.sourceKind = FeedsSource.PROGER
                        newsItem.id = getIdInLink(it.guid)
                        newsItem.link = it.link
                        newsItem.title = it.title
                        newsItem.date = it.date
                        newsItem.pictureLink = "https://tproger.ru/apple-touch-icon.png"
                        list.add(newsItem)
                    }
                }
            }

        }
        return Observable.fromArray(list).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
    }

    fun toNewsContent(hc: HabrContent): Content {
        return Content(
                id = null,
                descriptionId = null,
                contentText = hc.content
        )
    }

    fun toNewsContent(pc: ProgerContent): Content {
        return Content(
                id = null,
                descriptionId = pc.id,
                contentText = pc.content)
    }

}