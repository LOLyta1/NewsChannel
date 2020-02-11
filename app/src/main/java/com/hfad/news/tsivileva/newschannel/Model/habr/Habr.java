package com.hfad.news.tsivileva.newschannel.model.habr;



import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;
@Root(name="rss", strict=false)
public class Habr {
/*
* Класс для хранения информации о  Канале
* Выступает моделью в MVP
*/
   @Element(name="title")
        @Path("channel")
        private String channelTitle;


        @ElementList(name="item", inline=true)
        @Path("channel")
        /*список новостей канала (вложенный элемент)*/
        private List<HabrlItems> habrlItems;

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }

        public List<HabrlItems> getHabrlItems() {
            return habrlItems;
        }

        public void setHabrlItems(List<HabrlItems> habrlItems) {
            this.habrlItems = habrlItems;
        }



}
