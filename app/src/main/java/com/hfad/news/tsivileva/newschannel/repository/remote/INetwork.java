package com.hfad.news.tsivileva.newschannel.repository.remote;

import androidx.lifecycle.LiveData;

import com.hfad.news.tsivileva.newschannel.model.habr.Habr;
import com.hfad.news.tsivileva.newschannel.model.habr.HabrItemsInfo;
import com.hfad.news.tsivileva.newschannel.model.tproger.TProger;
import com.hfad.news.tsivileva.newschannel.model.tproger.TProgerItemsInfo;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.internal.operators.observable.ObservableAll;
import me.linshen.retrofit2.adapter.ApiResponse;
import retrofit2.http.GET;


/*
 * Интерфейс для получения данных о Model Presenter-ом по сети
 * */
public interface INetwork {

    @GET(".")
    Observable<Habr> loadHabr();

    @GET(".")
    Observable<TProger> loadProger();

    @GET(".")
    Single<HabrItemsInfo> loadHabrDetails();

    @GET(".")
    Single<TProgerItemsInfo> loadProgDetails();
}
