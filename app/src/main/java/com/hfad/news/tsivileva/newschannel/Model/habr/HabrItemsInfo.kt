package com.hfad.news.tsivileva.newschannel.model.habr;

import pl.droidsonroids.jspoon.annotation.Selector

class HabrItemsInfo {
    @Selector(".post__title-text")
    var title: String? = null

    @Selector(".post__time")
    var date: String? = null

    @Selector("#post-content-body")
    var content: String? = null
}