package com.hfad.news.tsivileva.newschannel.Model.implementation.tproger;

/*
* (Модель данных  TProger)
* Класс Для работы с https://tproger.ru/feed/
* Сгенирирован автоматически сервисом  http://pojo.sodhanalibrary.com/
* */
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "image", strict = false)
public class Atom {
    @Element(required = false)
    private String rel;
    @Element(required = false)
    private String href;

    @Element(required = false)
    private String type;

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClassPojo [rel = " + rel + ", href = " + href + ", type = " + type + "]";
    }
}
