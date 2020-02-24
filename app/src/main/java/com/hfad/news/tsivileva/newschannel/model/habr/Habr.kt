package com.hfad.news.tsivileva.newschannel.model.habr


import com.hfad.news.tsivileva.newschannel.toNonNullString
import org.simpleframework.xml.*
import java.text.SimpleDateFormat
import java.util.*

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
        var link: String = ""

        @field:Element(name = "description", required = false, data = true)
        //@Convert(HabrItemsConverter::class)
        var description: String? = null

        @field:Element(name = "pubDate", required = false)
        var dateString: String? = null

        var  date: Date?=null
        get() {
            dateString?.let {
                val from = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
                return from.parse(it)
            }
            return null
        }


        var image: String? = null
            get() = findImage()



        private fun findImage(): String? {
            var IMG_SRC_REG_EX = "<img src=\"([^>]+)\">"
            var imageUrl = Regex(IMG_SRC_REG_EX).find(description.toNonNullString(), 0)?.value.toString()

            if(imageUrl.isEmpty()){
                 IMG_SRC_REG_EX = "<img src=\"([^>]+)\"/>"
                imageUrl=Regex(IMG_SRC_REG_EX).find(description.toNonNullString(), 0)?.value.toString()
            }

            val start_tag = "<img src=\""
            val end_tag = "\""
            val start_pos = imageUrl.indexOf(start_tag)
            var end_pos=-1

            if (start_pos!=-1){
               end_pos=imageUrl.indexOf(end_tag, startIndex = start_pos+start_tag.length)
               if(end_pos!=-1){
                   imageUrl=imageUrl.substring(start_pos+start_tag.length,end_pos)
               }
            }
            return imageUrl
        }
    }
}



