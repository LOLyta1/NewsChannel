package com.hfad.news.tsivileva.newschannel.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.hfad.news.tsivileva.newschannel.R;
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeed;

public class MainActivity extends AppCompatActivity //implements IView {
{

    public static final String logname = "mylog";

    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setUpLeftArea(getSupportFragmentManager(), new FragmentFeed(this));
          }


    private void setUpLeftArea(final FragmentManager manager, final Fragment fragment) {
        mFragmentTransaction = manager.beginTransaction();
        mFragmentTransaction.add(R.id.container, fragment);
        mFragmentTransaction.commit();
    }


}
