
package com.hfad.news.tsivileva.newschannel.Model.implementation.tproger;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
/*
 * (Модель данных  TProger)
 * Класс Для работы с https://tproger.ru/feed/
 * Сгенирирован автоматически сервисом  http://pojo.sodhanalibrary.com/
 * */
@Root(name = "rss", strict = false)
public class TProger {
    @Element
    private Channel channel;

    @Element(required = false)
    private String version;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ClassPojo [channel = " + channel + ", version = " + version + "]";
    }
}
