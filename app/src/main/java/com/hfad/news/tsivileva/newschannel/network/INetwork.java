package com.hfad.news.tsivileva.newschannel.network;

import com.hfad.news.tsivileva.newschannel.Model.implementation.habr.Habr;
import com.hfad.news.tsivileva.newschannel.Model.implementation.habr.HabrItemsInfo;
import com.hfad.news.tsivileva.newschannel.Model.implementation.tproger.TProger;

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
    Observable<HabrItemsInfo> loadDetails();

    @GET(".")
    Observable<TProger>loadTProger();

}
