package com.hfad.news.tsivileva.newschannel.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import com.hfad.news.tsivileva.newschannel.FEED
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeeds

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        /*(menu.findItem(R.id.app_bar_search).actionView as EditText).apply{
            this.textCursorDrawable=null
            this.setTextColor(resources.getColor(R.color.searcher_color))
        }*/
        if (supportFragmentManager.findFragmentByTag(FEED) == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, FragmentFeeds(), FEED).commit()
        }

        supportActionBar?.title=""
    }
}