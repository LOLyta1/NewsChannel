package com.hfad.news.tsivileva.newschannel.View;

import com.hfad.news.tsivileva.newschannel.Adapter.ItemAdapter;

public interface IView {
    void showNews(ItemAdapter item);
    void showError(Throwable er);
    void showComplete();
}
