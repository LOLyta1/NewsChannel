package com.hfad.news.tsivileva.newschannel.Model.implementation.tproger;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
/*
 * (Модель данных  TProger)
 * Класс Для работы с https://tproger.ru/feed/
 * Сгенирирован автоматически сервисом  http://pojo.sodhanalibrary.com/
 * */
@Root(name = "image", strict = false)
public class Image {
    @Element
    private String link;

    @Element
    private String title;

    @Element
    private String url;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ClassPojo [link = " + link + ", title = " + title + ", url = " + url + "]";
    }
}