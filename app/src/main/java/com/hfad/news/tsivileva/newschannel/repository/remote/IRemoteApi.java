package com.hfad.news.tsivileva.newschannel.repository.remote;

import com.hfad.news.tsivileva.newschannel.model.habr.Habr;
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent;
import com.hfad.news.tsivileva.newschannel.model.proger.Proger;
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;


/*
 * Интерфейс для получения данных о Model Presenter-ом по сети
 * */
public interface IRemoteApi {
    @GET(".")
    Observable<Habr> loadHabr();

    @GET(".")
    Observable<Proger> loadProger();

    @GET(".")
    Observable<HabrContent> loadHabrContent();

    @GET(".")
    Observable<ProgerContent> loadProgDetails();

}
