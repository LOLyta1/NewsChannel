package com.hfad.news.tsivileva.newschannel.model.habr;

import pl.droidsonroids.jspoon.annotation.Selector

class HabrContent{
    @Selector(value=".post__title-text")
    var title: String? = null

    @Selector(value=".post__time")
    var date: String? = null

    @Selector(value="#post-content-body")
    var content: String? = null

    @Selector(value="img",attr = "src",index = 0)
    var image: String?=null
}