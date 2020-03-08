package com.hfad.news.tsivileva.newschannel.repository.remote;

import com.hfad.news.tsivileva.newschannel.model.remote.habr.Habr;
import com.hfad.news.tsivileva.newschannel.model.remote.habr.HabrContent;
import com.hfad.news.tsivileva.newschannel.model.remote.proger.Proger;
import com.hfad.news.tsivileva.newschannel.model.remote.proger.ProgerContent;

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
    Single<HabrContent> loadHabrContent();

    @GET(".")
    Single<ProgerContent> loadProgDetails();
}
