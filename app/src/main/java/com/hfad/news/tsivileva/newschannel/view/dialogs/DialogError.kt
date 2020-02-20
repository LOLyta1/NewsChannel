package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.R
import kotlinx.android.synthetic.main.dialog_network.view.*


class DialogError() : DialogFragment() {

    interface IDialogListener {
        fun onDialogReloadClick(dialog: DialogError)
        fun onDialogCancelClick(dialog: DialogError)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_network, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parent = parentFragment as IDialogListener
        view.update_button.setOnClickListener { parent.onDialogReloadClick(this) }
        view.cancel_button.setOnClickListener {parent.onDialogCancelClick(this) }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if(manager.findFragmentByTag(tag)==null){
            super.show(manager, tag)
        }
    }
}