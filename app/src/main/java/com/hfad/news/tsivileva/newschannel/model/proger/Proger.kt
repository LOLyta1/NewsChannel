package com.hfad.news.tsivileva.newschannel.model.proger

import org.simpleframework.xml.*

@Root(name = "rss", strict = false)
class Proger {
    @field:Element
    var channel: Channel? = null
    @field:Element(required = false)
    var version: String? = null

    override fun toString(): String {
        return "ClassPojo [channel = $channel, version = $version]"
    }
    @Root(name = "channel", strict = false)
    class Channel {
        @field:Element()
        var image: Image? = null
        @field:ElementList(inline = true, required = false)
        var items: List<Item>? = null
        @field:Element(required = false)
        var lastBuildDate: String? = null
        @field:Element(required = false)
        var link: String? = null
        @field:Element(required = false)
        var description: String? = null
        @field:Element(required = false)
        var generator: String? = null
        @field:Element(required = false)
        var language: String? = null
        @field:Element(required = false)
        var title: String? = null

        override fun toString(): String {
            return "ClassPojo [image = " + image + ", item = " + items + ", lastBuildDate = " + lastBuildDate + ", link = " + link + ", description = " + description + ", generator = " + generator + ", language = " + language + ", title = " + title + "]"
        }

        @Root(name = "image", strict = false)
        class Image {
            @field:Element
            var link: String? = null
            @field:Element
            var title: String? = null
            @field:Element
            var url: String? = null

            override fun toString(): String {
                return "ClassPojo [link = $link, title = $title, url = $url]"
            }
        }
        @Root(name = "item", strict = false)
        class Item {
            @field:Path("comments")
            @field:Text(required = false)
            var comments: String? = null
            @field:Element(required = false)
            var link: String? = null
            @field:Element(required = false)
            var guid: Guid? = null
            @field:Element(required = false)
            var description: String? = null
            @field:Element(required = false)
            var title: String? = null
            @field:Path("category")
            @field:Text(required = false)
            var category: String? = null
            @field:Element(required = false)
            var pubDate: String? = null

            override fun toString(): String {
                return "ClassPojo [comments = $comments, link = $link, guid = $guid, description = $description, title = $title, category = $category, pubDate = $pubDate]"
            }
            @Root(name = "image", strict = false)
            class Guid {
                @field:Element(required = false)
                var isPermaLink: String? = null
                @field:Element(required = false)
                var content: String? = null

                override fun toString(): String {
                    return "ClassPojo [isPermaLink = $isPermaLink, content = $content]"
                }
            }
        }
    }
}
@Root(name = "image", strict = false)
class Atom {
    @field:Element(required = false)
    var rel: String? = null
    @field:Element(required = false)
    var href: String? = null
    @field:Element(required = false)
    var type: String? = null

    override fun toString(): String {
        return "ClassPojo [rel = $rel, href = $href, type = $type]"
    }
}


@Root(name = "image", strict = false)
class Guid {
    @field:Element(required = false)
    var isPermaLink: String? = null
    @field:Element(required = false)
    var content: String? = null

    override fun toString(): String {
        return "ClassPojo [isPermaLink = $isPermaLink, content = $content]"
    }
}