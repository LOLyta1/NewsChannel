package com.hfad.news.tsivileva.newschannel.Presenter;

import android.util.Log;

import com.hfad.news.tsivileva.newschannel.Adapter.ItemAdapter;
import com.hfad.news.tsivileva.newschannel.Model.implementation.Habr.Habr;

import com.hfad.news.tsivileva.newschannel.Model.implementation.Habr.HabrlItems;
import com.hfad.news.tsivileva.newschannel.Network.INetwork;
import com.hfad.news.tsivileva.newschannel.Network.NetworkClientHabr;
import com.hfad.news.tsivileva.newschannel.View.IView;
import com.hfad.news.tsivileva.newschannel.View.MainActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HabrPresenter implements IPresenter {

    /*ссылки на интерфейс view*/
    private IView mView;

    public HabrPresenter(IView mView) {

        this.mView = mView;
        Log.d(MainActivity.logname,"Конструктор HabrPresenter()");

    }


    /*
     *Метод, подписывающий подписчика на источник
     * т.е. инициализирует выполнение запросов к серверу в отдельном потоке, а обработку данных и
     * отправку их в представление — через главный поток
     * */
    @Override
    public void getNews(boolean isUpdate) {
        if(isUpdate){
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
    private Observable<Habr> getObservableHabr(){
        return NetworkClientHabr.
                getRetrofit().
                create(INetwork.class).
                loadNews().
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
    private DisposableObserver<Habr> getObserverHabr(){

        return new DisposableObserver<Habr>() {
            @Override
            public void onNext(Habr habr) {

                if(habr.getHabrlItems().size()>0){

                    int last=habr.getHabrlItems().size()-1;
                    for (HabrlItems i:habr.getHabrlItems()
                         ) {
                        ItemAdapter item=new ItemAdapter();
                        item.setDate(i.getDate());
                        item.setTitle(i.getTitle());
                        item.setLink(i.getLink());
                        item.setPicture(i.getHabrItemsDetail().getImageSrc());
                        item.setSummarry(i.getHabrItemsDetail().getDescription());
                        mView.showNews(item);
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
