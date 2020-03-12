package com.hfad.news.tsivileva.newschannel.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import com.hfad.news.tsivileva.newschannel.FEED
import com.hfad.news.tsivileva.newschannel.FEED_CONTENT
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeeds

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.title=""
        if (supportFragmentManager.findFragmentByTag(FEED) == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, FragmentFeeds(), FEED).commit()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragment=supportFragmentManager.findFragmentByTag(FEED_CONTENT)

            (fragment as? IPermissionListener)?. getPermissions(requestCode,permissions,grantResults)

    }

}