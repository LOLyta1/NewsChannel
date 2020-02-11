package com.hfad.news.tsivileva.newschannel.network;

import com.hfad.news.tsivileva.newschannel.model.habr.HabrItemsDetailConverter;
import com.hfad.news.tsivileva.newschannel.model.habr.HabrItemsInfo;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


/*
 * Класс для создания подключения к Habrahabr
 */
public class NetworkClientHabr {

    public static Retrofit retrofit;


    public static Retrofit getRetrofit( ){

        /*поскольку на хабре ссылка на картинку указана прямо внутри блока описания,

        * то указываем дополнительно собственный адаптер для парсинга:*/
        Registry registry = new Registry();
        Strategy strategy = new RegistryStrategy(registry);
        Serializer serializer = new Persister(strategy);
        try {
            registry.bind(HabrItemsInfo.class, HabrItemsDetailConverter.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*адаптор для выполнения запросов не в GUI-потоке, а через планировщик*/
        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

        if(retrofit==null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            OkHttpClient okHttpClient = builder.build();

            /*в качестве конвертора данных добавляем SimpleXmlConverterFactory
            * а так же собственный конвертор SimpleXmlConverterFactory.create(serializer) */
            retrofit = new Retrofit.Builder()
                     .baseUrl("https://habr.com/ru/rss/all/")
                    .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(rxAdapter)
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
