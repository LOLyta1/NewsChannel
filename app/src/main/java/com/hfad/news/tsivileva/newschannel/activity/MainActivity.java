package com.hfad.news.tsivileva.newschannel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.hfad.news.tsivileva.newschannel.R;
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeed;

public class MainActivity extends AppCompatActivity //implements IView {
{

    public static final String logname = "mylog";

    private FragmentTransaction mFragmentTransaction;
    private FragmentFeed fragmentFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        fragmentFeed=new FragmentFeed();
        mFragmentTransaction= getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.container, fragmentFeed,"feed_fragment");
        mFragmentTransaction.commit();
  }


}
