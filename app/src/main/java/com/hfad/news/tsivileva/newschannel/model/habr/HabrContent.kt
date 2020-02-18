package com.hfad.news.tsivileva.newschannel.model.habr;

import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector

class HabrContent {
    @Selector(value = "html")
    val htmlElement: Element? = null


    @Selector(value = ".post__title-text")
    var title: String? = null

    @Selector(value = ".post__time")
    var date: String? = null

    @Selector(value = "#post-content-body")
    var content: String? = null


    var image: String? = null
    get()=findImage(htmlElement)


private fun findImage(el: Element?) :String?{
    el?.getElementsByTag("div")?.forEach {
        it.getElementsByClass("post__text post__text-html").forEach {
           return it.getElementsByTag("img").attr("src")
        }
    }
    return null
}


    var link: String? = null
        get() = findLink(htmlElement)

    private fun findLink(el: Element?): String {
        var link = ""
        el?.getElementsByTag("meta")?.forEach {
            it.getElementsByAttributeValueStarting("property", "og:url").forEach {
                link = it.attr("content").toString()
            }
        }
        return link
    }

}