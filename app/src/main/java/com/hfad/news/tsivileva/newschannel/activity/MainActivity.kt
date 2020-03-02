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

        if (supportFragmentManager.findFragmentByTag(FEED) == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, FragmentFeeds(), FEED).commit()
        }
      //  val Toolbar=Toolbar(this).apply{
       //     setMenu(MenuBuilder(this@MainActivity).add)
     //   }
       // setSupportActionBar(Toolbar(this))
        supportActionBar?.title=""
    }
}