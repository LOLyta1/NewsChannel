package com.hfad.news.tsivileva.newschannel.view;

import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem;

public interface IView {
    void showNews();
    void showError(Throwable er);
    void showComplete();
}
