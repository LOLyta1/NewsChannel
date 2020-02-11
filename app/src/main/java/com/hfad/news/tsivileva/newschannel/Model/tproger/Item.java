package com.hfad.news.tsivileva.newschannel.model.tproger;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
/*
 * (Модель данных  TProger)
 * Класс Для работы с https://tproger.ru/feed/
 * Сгенирирован автоматически сервисом  http://pojo.sodhanalibrary.com/
 * */
@Root(name = "item", strict = false)
public class Item {

    @Path("comments")
    @Text(required=false)
    private String comments;

    @Element(required = false)
    private String link;

    @Element(required = false)
    private Guid guid;

    @Element(required = false)
    private String description;

    @Element(required = false)
    private String title;

    @Path("category")
    @Text(required=false)
    private String category;

    @Element(required = false)
    private String pubDate;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Guid getGuid() {
        return guid;
    }

    public void setGuid(Guid guid) {
        this.guid = guid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "ClassPojo [comments = " + comments + ", link = " + link + ", guid = " + guid + ", description = " + description + ", title = " + title + ", category = " + category + ", pubDate = " + pubDate + "]";
    }
}

