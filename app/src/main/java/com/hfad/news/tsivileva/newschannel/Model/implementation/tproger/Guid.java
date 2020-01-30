
package com.hfad.news.tsivileva.newschannel.Model.implementation.tproger;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
/*
 * (Модель данных  TProger)
 * Класс Для работы с https://tproger.ru/feed/
 * Сгенирирован автоматически сервисом  http://pojo.sodhanalibrary.com/
 * */
@Root(name = "image", strict = false)
public class Guid {
    @Element(required = false)
    private String isPermaLink;

    @Element(required = false)
    private String content;

    public String getIsPermaLink() {
        return isPermaLink;
    }

    public void setIsPermaLink(String isPermaLink) {
        this.isPermaLink = isPermaLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ClassPojo [isPermaLink = " + isPermaLink + ", content = " + content + "]";
    }
}