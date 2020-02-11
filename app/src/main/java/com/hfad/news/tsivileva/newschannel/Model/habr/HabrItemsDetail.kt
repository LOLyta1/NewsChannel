package com.hfad.news.tsivileva.newschannel.model.habr

/*
 * Класс для хранения данных о картинке и описании статьи на Habr
 */
data class HabrItemsDetail (
        /*ссылка на картинку*/
        var imageSrc: String? = null,
        /*описание новости*/
        var description: String? = null
)
