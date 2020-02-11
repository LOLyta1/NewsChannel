package com.hfad.news.tsivileva.newschannel.model.habr

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root
import org.simpleframework.xml.convert.Convert

@Root(name = "rss", strict = false)
class Habr {

    @field:ElementList(name = "item", inline = true)
    @field:Path("channel")
    var habrlItems: List<HabrlItems>? = null

        @Root(name = "item", strict = false)
        class HabrlItems(){

            @field:Element(name = "title", required = false)
            var title: String? = null

            @field:Element(name = "link", required = false)
            var link: String? = null

            @field:Element(name = "description", required = false)
            @Convert(HabrItemsDetailConverter::class)
            var habrItemsDetail: HabrItemsDetail? = null

            @field:Element(name = "pubDate", required = false)
            var date: String? = null
        }
}
