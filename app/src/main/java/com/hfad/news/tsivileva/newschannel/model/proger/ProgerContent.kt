package com.hfad.news.tsivileva.newschannel.model.proger;


import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.getIdInLink
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector

class ProgerContent {
    @Selector(value="html")
    val element: org.jsoup.nodes.Element?=null


    @Selector(".entry-content")
    var content:String?=null

    @Selector(".entry-date")
    var date:String?=null

    @Selector(".entry-title")
    var title:String?=null

    var image= "https://tproger.ru/apple-touch-icon.png"


    var link:String ?=null
    get()=findLink(element)

    var id:Long?=null
    get()=findId(element)

    private fun findLink(el: Element?): String {
        var link=""
        el?.getElementsByTag("link")?.forEach {
            it.getElementsByAttributeValueStarting("rel","canonical").forEach {
                link=it.attr("href").toString()
            }
        }
        return link
    }

    private fun findId(el: Element?):Long?{
        var id:Long?=0L
        el?.getElementsByTag("link")?.forEach {
         id= getIdInLink(it.attr("id").toString())
        }
        return id
    }
}