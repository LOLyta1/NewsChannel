package com.hfad.news.tsivileva.newschannel.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.hfad.news.tsivileva.newschannel.Adapter.ItemAdapter;
import com.hfad.news.tsivileva.newschannel.Adapter.AdapterNews;
import com.hfad.news.tsivileva.newschannel.Presenter.HabrPresenter;
import com.hfad.news.tsivileva.newschannel.Presenter.ProgerPresenter;
import com.hfad.news.tsivileva.newschannel.R;

public class MainActivity extends AppCompatActivity implements IView {

    public static final String logname="mylog";


    private   RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdapterNews adapter=new AdapterNews();
        mRecyclerView =findViewById(R.id.resycler_view);
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        HabrPresenter hpr=new HabrPresenter(this);
        hpr.getNews(true);

        ProgerPresenter pr=new ProgerPresenter(this);
        pr.getNews(true);
   }


/*
* реализация интерфейса IView
*  методы срабатывают, когда один из Presenter получит данные о Model и
*  выполнит вызов метода
* */
    @Override
    public void showNews(ItemAdapter i) {
             ((AdapterNews)  mRecyclerView.getAdapter()).add(i);

    }

    @Override
    public void showError(Throwable er) {
        Log.d(MainActivity.logname,"Ошибка"+er.getMessage());

    }

    @Override
    public void showComplete() {
        Log.d(MainActivity.logname,"Закончено");

    }
}
