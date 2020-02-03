package com.hfad.news.tsivileva.newschannel.view;

import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem;

public interface IView {
    void showNews(NewsItem newsItem);
    void showError(Throwable er);
    void showComplete();
    void reloadNews(IView view);

}
