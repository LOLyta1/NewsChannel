package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hfad.news.tsivileva.newschannel.R
import kotlinx.android.synthetic.main.dialog_network.view.*


class DialogNet() : DialogFragment() {

    interface INetworkDialogListener {
        fun uploadClick(dialog: DialogNet)
        fun cancelClick(dialog: DialogNet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_network, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val target = this.targetFragment as? INetworkDialogListener
        view.update_button.setOnClickListener { target?.uploadClick(this) }
        view.cancel_button.setOnClickListener { target?.cancelClick(this) }
    }
}