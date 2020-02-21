package com.hfad.news.tsivileva.newschannel.model.proger;
import android.util.Log
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.getIdInLink
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector

class ProgerContent {

    @Selector(value = ".entry-content")
    var content: String? = null

    @Selector(value = "head")
    var dateElements:Element?=null

    var date: String? = null
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

    fun findDate():String{
        Log.d(DEBUG_LOG,"атрибут - ${dateElements.toString()}")
        dateElements?.getElementsByAttributeValue("property","og:updated_time")?.forEach {
            Log.d(DEBUG_LOG,"атрибут - ${it.attr("content")}")
            return it.attr("content")
        }
        return ""
    }
}