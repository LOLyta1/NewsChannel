package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.R
import kotlinx.android.synthetic.main.dialog_network.view.*


class DialogNetworkError() : DialogFragment() {

    interface IDialogListener {
        fun onDialogErrorReloadClick(dialogNetwork: DialogNetworkError)
        fun onDialogErrorCancelClick(dialogNetwork: DialogNetworkError)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_network, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parent = parentFragment as IDialogListener
        view.update_button.setOnClickListener { parent.onDialogErrorReloadClick(this) }
        view.cancel_button.setOnClickListener { parent.onDialogErrorCancelClick(this) }
    }


}