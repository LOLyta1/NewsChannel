package com.hfad.news.tsivileva.newschannel.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hfad.news.tsivileva.newschannel.FEED
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeeds

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (supportFragmentManager.findFragmentByTag(FEED) == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, FragmentFeeds(), FEED).commit()
        }
        supportActionBar?.title=""
    }
}