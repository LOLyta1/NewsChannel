package com.hfad.news.tsivileva.newschannel.model.remote.proger;

import android.util.Log
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.getIdInLink
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector
import java.text.SimpleDateFormat
import java.util.*

class ProgerContent {

    @Selector(value = "head")
    var linkElement: Element? = null

    val link: String
        get() {
            var attr = ""
            linkElement?.getElementsByAttributeValue("rel", "canonical")?.forEach {
                attr = it.attr("href")
            }
            return attr
        }


    @Selector(value = ".entry-content")
    var content: String = ""

    @Selector(value = "head")
    var dateElements: Element? = null

    var date: Date? = null
        get() = findDate()

    @Selector(value = ".entry-title")
    var title: String = ""

    var image = "https://tproger.ru/apple-touch-icon.png"

    @Selector(value = "article", attr = "id")
    var idElement: Element? = null

    var id: Long = 0L
        get() = findID(idElement)

    fun findID(element: Element?): Long {
        return getIdInLink(element?.attr("id"))
    }

    fun findDate(): Date? {
        var dateString = ""
        //Log.d(DEBUG_LOG,"атрибут - ${dateElements.toString()}")
        dateElements?.getElementsByAttributeValue("property", "og:updated_time")?.forEach {
            Log.d(DEBUG_LOG, "атрибут - ${it.attr("content")}")
            dateString = it.attr("content")
        }
        Log.d(DEBUG_LOG, "Дата для парсинга:- $dateString")

        val startGmtIndex = dateString.indexOf("+")
        var gmt = dateString.substring(startGmtIndex, dateString.length)
        gmt = gmt.replace(":", "")

        dateString = dateString.substring(0, startGmtIndex) + gmt
        Log.d(DEBUG_LOG, "Обрезанная дата для парсинга:- $dateString")
        val from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
        return from.parse(dateString)
    }
}