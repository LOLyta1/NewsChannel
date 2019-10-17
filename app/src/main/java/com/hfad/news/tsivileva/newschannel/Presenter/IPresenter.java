package com.hfad.news.tsivileva.newschannel.Presenter;

/*
* Интерфейс для получения уведомления Presenter-а от View
* о том, что необходимо запросить данные о Model через соединение
* (класс, реализующий интерфейс INetwork)
* */
public interface IPresenter {
     void getNews(boolean isUpdate);

}
