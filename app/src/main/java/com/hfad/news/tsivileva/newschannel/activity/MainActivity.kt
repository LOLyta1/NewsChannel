package com.hfad.news.tsivileva.newschannel.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.hfad.news.tsivileva.newschannel.FEED
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeed
import com.hfad.news.tsivileva.newschannel.view_model.FeedViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (supportFragmentManager.findFragmentByTag(FEED) == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, FragmentFeed(), FEED).commit()
        }
    }
}