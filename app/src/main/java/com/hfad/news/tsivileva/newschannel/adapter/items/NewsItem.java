package com.hfad.news.tsivileva.newschannel.adapter.items;


/*
Класс для хранения элемента списка ResyclerView
 */

public class NewsItem {
    private String date;
    private String title;
    private String summarry;
    private String picture;
    private String link;

    public NewsItem() {
    }

    public NewsItem(String date, String title, String summarry, String picture, String link) {
        this.date = date;
        this.title = title;
        this.summarry = summarry;
        this.picture = picture;
        this.link = link;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummarry() {
        return summarry;
    }

    public void setSummarry(String summarry) {
        this.summarry = summarry;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
