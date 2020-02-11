package com.hfad.news.tsivileva.newschannel.network;

import com.hfad.news.tsivileva.newschannel.model.habr.Habr;
import com.hfad.news.tsivileva.newschannel.model.habr.HabrItemsInfo;
import com.hfad.news.tsivileva.newschannel.model.tproger.TProger;
import com.hfad.news.tsivileva.newschannel.model.tproger.TProgerItemsInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;


/*
 * Интерфейс для получения данных о Model Presenter-ом по сети
 * */
public interface INetwork {

    /*параметры для get-запроса - знак точки,т.е.весь адрес полностью*/
    /*формат запрашиваемых данных - observable*/

    @GET(".")
    Observable<Habr> loadNews();

    @GET(".")
    Observable<TProger>loadTProger();

    @GET(".")
    Observable<HabrItemsInfo> loadHabrDetails();

    @GET(".")
    Observable<TProgerItemsInfo> loadProgDetails();
}
