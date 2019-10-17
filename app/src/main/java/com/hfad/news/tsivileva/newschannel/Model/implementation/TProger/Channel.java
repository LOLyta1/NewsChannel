
package com.hfad.news.tsivileva.newschannel.Model.implementation.TProger;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
/*
 * (Модель данных  TProger)
 * Класс Для работы с https://tproger.ru/feed/
 * Сгенирирован автоматически сервисом  http://pojo.sodhanalibrary.com/
 * */
@Root(name = "channel", strict = false)
public class Channel {
    @Element
    private Image image;

    @ElementList(inline = true, required = false)
    private List<Item> itemList;

    @Element(required = false)
    private String lastBuildDate;

    @Element(required = false)
    private String link;

    @Element(required = false)
    private String description;

    @Element(required = false)
    private String generator;

    @Element(required = false)
    private String language;

    @Element(required = false)
    private String title;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Item> getItem() {
        return itemList;
    }

    public void setItem(List<Item> itemList) {
        this.itemList = itemList;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ClassPojo [image = " + image + ", item = " + itemList + ", lastBuildDate = " + lastBuildDate + ", link = " + link + ", description = " + description + ", generator = " + generator + ", language = " + language + ", title = " + title + "]";
    }
}
