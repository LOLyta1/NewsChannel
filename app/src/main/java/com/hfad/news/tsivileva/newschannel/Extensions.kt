package com.hfad.news.tsivileva.newschannel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.adapter.Sources
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeed
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentNetworkError
import kotlinx.android.synthetic.main.fragment_feed.view.*
import java.lang.Exception
import java.lang.NullPointerException

val FRAGMENT_WITH_FEED = "fragment_with_feed"
val FRAGMENT_WITH_FEED_CONTENT = "fragment_with_feed_content"
val FRAGMENT_WITH_ERROR_DOWNLOADING_FEED = "fragment_with_error_downloading_feed"
val FRAGMENT_WITH_ERROR_DOWNLOADING_FEED_CONTENT = "fragment_with_error_downloading_feed_content"
val DIALOG_FRAGMENT_WITH_ERROR = "dialog_with_error"


fun Fragment.showDialogError(manager: FragmentManager,
                             dialogTag: String) {
    val dialog = DialogError()
    dialog.show(manager, dialogTag)
}

fun String?.toNonNullString(): String {
    if (this == null) {
        val exception = NullPointerException("URL is empty!").apply { printStackTrace() }
        throw exception
    } else {
        return this
    }
}

fun getIdInLink(link: String?): Int? {
    link?.let {
        return Regex("[0-9]{6,8}").find(link, 0)?.value?.toInt()
    }
    return null
}

fun showErrorFragment(fragmentManager: FragmentManager, containerId: Int, tag: String) {
    val fragment = FragmentNetworkError()
    if (fragmentManager.findFragmentByTag(tag) == null) {
        fragmentManager.beginTransaction().add(containerId, fragment, tag).addToBackStack(tag).commit()
    }
}

fun removeFragmentError(fragmentManager: FragmentManager, tag: String) {
    val fragment = fragmentManager.findFragmentByTag(tag)
    fragment?.let {
        fragmentManager.beginTransaction().remove(fragment).commit()
    }
    printFragmentsInManager(fragmentManager)
}





