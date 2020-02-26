package com.hfad.news.tsivileva.newschannel.model.proger;
import android.util.Log
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.getIdInLink
import com.hfad.news.tsivileva.newschannel.model.IModel
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector
import java.text.SimpleDateFormat
import java.util.*

class ProgerContent :IModel{

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
              //Log.d(DEBUG_LOG,"атрибут - ${dateElements.toString()}")
        dateElements?.getElementsByAttributeValue("property","og:updated_time")?.forEach {
            Log.d(DEBUG_LOG,"атрибут - ${it.attr("content")}")
           dateString= it.attr("content")
        }
        Log.d(DEBUG_LOG,"Дата для парсинга:- $dateString")

        val startGmtIndex=dateString.indexOf("+")
        var gmt=dateString.substring(startGmtIndex,dateString.length)
        gmt=gmt.replace(":","")

        dateString=dateString.substring(0, startGmtIndex)+gmt
        Log.d(DEBUG_LOG,"Обрезанная дата для парсинга:- $dateString")
        val from=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",Locale.US)
       return from.parse(dateString)
    }
}