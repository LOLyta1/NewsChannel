package com.hfad.news.tsivileva.newschannel.model.habr;

import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.annotation.Selector

class HabrContent{
    @Selector(value="html")
    val element: Element?=null

    @Selector(value=".post__title-text")
    var title: String? = null

    @Selector(value=".post__time")
    var date: String? = null

    @Selector(value="#post-content-body")
    var content: String? = null

    @Selector(value="img",attr = "src",index = 0)
    var image: String?=null

    var link:String?=null
    get()=findLink(element)

      fun findLink(el: Element?) :String{
        var link=""
        el?.getElementsByTag("meta")?.forEach {
            it.getElementsByAttributeValueStarting("property","og:url").forEach {
                link=it.attr("content").toString()
            }
        }
        return link
    }

}