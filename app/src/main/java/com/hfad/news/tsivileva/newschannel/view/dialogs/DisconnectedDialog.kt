package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.presenter.HabrPresenter
import com.hfad.news.tsivileva.newschannel.presenter.ProgerPresenter
import com.hfad.news.tsivileva.newschannel.view.IView
import kotlinx.android.synthetic.main.dialog_network.view.*

class DisconnectedDialog(val view: IView) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.dialog_network, container)
        mView.refresh_button.setOnClickListener {
            view.reloadNews(view)
            this.dismiss()
        }

        mView.cancel_button.setOnClickListener {
            this.dismiss()
        }
        return mView
    }
}