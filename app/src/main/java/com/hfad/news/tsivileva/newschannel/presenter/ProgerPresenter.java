package com.hfad.news.tsivileva.newschannel.presenter;

import android.util.Log;


import com.hfad.news.tsivileva.newschannel.Model.implementation.tproger.TProger;
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem;
import com.hfad.news.tsivileva.newschannel.network.INetwork;

import com.hfad.news.tsivileva.newschannel.network.NetworkClientProger;
import com.hfad.news.tsivileva.newschannel.view.IView;
import com.hfad.news.tsivileva.newschannel.view.MainActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ProgerPresenter implements IPresenter {

    /*ссылки на интерфейс view*/
    private IView mView;

    public ProgerPresenter(IView mView) {
        this.mView = mView;
        Log.d(MainActivity.logname,"");

    }


/*
*Метод, подписывающий подписчика на источник
* т.е. инициализирует выполнение запросов к серверу в отдельном потоке, а обработку данных и
* отправку их в представление — через главный поток
* */
    @Override
    public void getNews(boolean isUpdate) {
        if(isUpdate) {
            getObservableHabr().subscribeWith(getObserverHabr());
        }
    }


/*
*  Метод, создающий источник данных (подключение по сети).
* В используемом подключении источник данных будет уведомлять
* своих получателей  разных потоках:
* 1.Schedulers.io()- планировщик с неограниченным пулом+кшированием
*  2. AndroidSchedulers.mainThread() - специальный планировщик потоков, специфичных для android
* о состоянии источника данных
*
* */
    private Observable<TProger> getObservableHabr(){
        return NetworkClientProger.
                getRetrofit().
                create(INetwork.class).
                loadTProger().
                observeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());

    }


/*
*Метод, создающий подписчика (получателя) данных
*
*Для получателя, в его методе получения порции данных (onNext)
* заносит данные из иточника в объект класса ItemAdapter ()
* и инициализирует обновление данных в MainActivity путем вызова mView.showNews(item)
*
*/
    private DisposableObserver<TProger> getObserverHabr(){

        return new DisposableObserver<TProger>() {
            @Override
            public void onNext(TProger prog) {

                 if(prog.getChannel().getItem().size()>0){

                    for (com.hfad.news.tsivileva.newschannel.Model.implementation.tproger.Item i:prog.getChannel().getItem()
                         ) {

                        NewsItem newsItem =new NewsItem();
                        newsItem.setDate(i.getPubDate());
                        newsItem.setTitle(i.getTitle());
                        newsItem.setLink(i.getLink());
                        newsItem.setPicture("https://pbs.twimg.com/profile_images/857551974442651648/D5cZLXTf.jpg");
                        newsItem.setSummarry(i.getDescription());

                        mView.showNews(newsItem);
                    }

                }
            }

            @Override
            public void onError(Throwable e) {
                /*при возникновении ошибки инициализирует во View Toast*/
                mView.showError(e);
            }

            @Override
            public void onComplete() {
                /*при завершении работы источника инициализирует во View выполнение метода showComplete()*/
                mView.showComplete();
            }
        };
    }



}
