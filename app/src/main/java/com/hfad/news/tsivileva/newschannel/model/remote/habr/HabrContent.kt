package com.hfad.news.tsivileva.newschannel.model.remote.habr;

import com.hfad.news.tsivileva.newschannel.getIdInLink
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector
import java.text.SimpleDateFormat
import java.util.*

class HabrContent {
    val id: Long
        get() = getIdInLink(link)

    @Selector(value = "html")
    val htmlElement: Element? = null

    @Selector(value = ".post__title-text")
    var title: String = ""

    @Selector(value = ".post__time", attr = "data-time_published")
    var dateString: String = ""

    val date: Date?
        get() {
            val from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US)
            return from.parse(dateString)
        }


    @Selector(value = "#post-content-body")
    var content: String = ""


    val image: String
        get() = findImage(htmlElement)

    private fun findImage(el: Element?): String {
        var image = ""
        el?.getElementsByTag("div")?.forEach {
            it.getElementsByClass("post__text post__text-html").forEach {
                image = it.getElementsByTag("img").attr("src")
            }
        }
        return image
    }


    val link: String
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