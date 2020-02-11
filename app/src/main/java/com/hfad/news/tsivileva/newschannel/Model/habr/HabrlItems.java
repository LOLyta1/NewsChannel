package com.hfad.news.tsivileva.newschannel.model.habr;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

@Root(name="item", strict = false)
public class HabrlItems {

    /*класс для хранени информации о новости*/


    public HabrlItems() {
    }

    @Element(name = "title", required=false)
    private String title;

    @Element(name = "link", required=false)
    private String link;

   @Element(name="description", required=false)
   @Convert(HabrItemsDetailConverter.class)
   HabrItemsDetail habrItemsDetail;

    @Element(name="pubDate", required=false)
    private String date;



    public HabrlItems(String title, HabrItemsDetail habrItemsDetail) {
        this.title = title;
        this.habrItemsDetail = habrItemsDetail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HabrItemsDetail getHabrItemsDetail() {
        return habrItemsDetail;
    }

    public void setHabrItemsDetail(HabrItemsDetail habrItemsDetail) {
        this.habrItemsDetail = habrItemsDetail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
