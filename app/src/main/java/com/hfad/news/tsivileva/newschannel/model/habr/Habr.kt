package com.hfad.news.tsivileva.newschannel.model.habr

import com.hfad.news.tsivileva.newschannel.toNonNullString
import org.simpleframework.xml.*

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

        @field:Element(name = "guid", required = false)
        var link: String? = null

        @field:Element(name = "description", required = false, data = true)
        //@Convert(HabrItemsConverter::class)
        var description: String? = null

        @field:Element(name = "pubDate", required = false)
        var date: String? = null

        var image: String? = null
            get() = findImage()


   private fun findImage(): String? {
            val IMG_SRC_REG_EX = "<img src=\"([^>]+)\">"
            var imageUrl = Regex(IMG_SRC_REG_EX).find(description.toNonNullString(), 0)?.value.toString()
           imageUrl= imageUrl.apply {
                replace(Regex("<img src=[^>]"), " ")
                replace(Regex("\" alt=\"[^>]"), " ")
            }
            return imageUrl
        }
    }


}



