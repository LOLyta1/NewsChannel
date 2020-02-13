package com.hfad.news.tsivileva.newschannel.model.habr

import android.util.Log
import org.simpleframework.xml.*
import org.simpleframework.xml.convert.Convert
import org.simpleframework.xml.strategy.Type
import org.simpleframework.xml.strategy.Visitor
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.NodeMap
import org.simpleframework.xml.stream.OutputNode

@Root(name = "rss", strict = false)
class Habr {

    @field:ElementList(name = "item", inline = true)
    @field:Path("channel")
    var items: List<HabrlItems>? = null

    /*Одна новость на странице:*/
    @Root(name = "item", strict = false)
    class HabrlItems() {

        @field:Element(name = "title", required = false)
        var title: String? = null

        @field:Element(name = "link", required = false)
        var link: String? = null

        @field:Element(name = "description", required = false, data = true)
        //@Convert(HabrItemsConverter::class)
        var habrItemsDetail: HabrItemsDetail? = null

        @field:Element(name = "pubDate", required = false)
        var date: String? = null


        /*Описание и картинка для каждой новости*/
        @Root(name = "description")
        class HabrItemsDetail {
            @field:Element(required = false)
            var imageSrc: String? = null

            @field:Element(required = false)
            var description: String? = null
        }
    }

}



