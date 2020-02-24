package com.hfad.news.tsivileva.newschannel.model.proger;
import android.util.Log
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.getIdInLink
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector
import java.text.SimpleDateFormat
import java.util.*

class ProgerContent {

    @Selector(value = ".entry-content")
    var content: String? = null

    @Selector(value = "head")
    var dateElements:Element?=null

    var date: Date? = null
    get() =findDate()

    @Selector(value =".entry-title")
    var title: String? = null

    var image ="https://tproger.ru/apple-touch-icon.png"

    @Selector(value = "article", attr = "id")
    var idElement: Element? = null

    var id: Long? = 0L
        get() = findID(idElement)

    fun findID(element: Element?): Long? {
        return getIdInLink(element?.attr("id"))
    }

    fun findDate():Date?{
        var dateString=""
        Log.d(DEBUG_LOG,"атрибут - ${dateElements.toString()}")
        dateElements?.getElementsByAttributeValue("property","og:updated_time")?.forEach {
            Log.d(DEBUG_LOG,"атрибут - ${it.attr("content")}")
           dateString= it.attr("content")
        }
       val from= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX",Locale.US)
       return from.parse(dateString)
    }
}