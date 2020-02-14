package com.hfad.news.tsivileva.newschannel.model.proger;

import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import pl.droidsonroids.jspoon.annotation.Selector

class ProgerContent {
    @Selector(".entry-content")
    var content:String?=null

    @Selector(".entry-date")
    var date:String?=null

    @Selector(".entry-title")
    var title:String?=null

    var image= "https://tproger.ru/apple-touch-icon.png"

}