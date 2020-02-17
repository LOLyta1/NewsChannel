package com.hfad.news.tsivileva.newschannel.model.proger;


import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
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

    private fun findLink(el: Element?): String {
        var link=""
        el?.getElementsByTag("link")?.forEach {
            it.getElementsByAttributeValueStarting("rel","canonical").forEach {
                link=it.attr("href").toString()
            }
        }
        return link
    }
}