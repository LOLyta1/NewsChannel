package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hfad.news.tsivileva.newschannel.R
import kotlinx.android.synthetic.main.dialog_network.view.*

interface INetworkDialogListener {
    fun uploadClick()
    fun cancelClick()
}

class DisconnectedDialog() : DialogFragment() {

    private lateinit var dialogView : View
    private var dialogListener:INetworkDialogListener?=null

//    fun setListener(listener: INetworkDialogListener){
//            dialogListener=listener
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView=inflater.inflate(R.layout.dialog_network, container)
        dialogView.refresh_button.setOnClickListener {
            val t = targetFragment as? INetworkDialogListener
            t?.uploadClick()
        }
        dialogView.cancel_button.setOnClickListener { dialogListener?.cancelClick() }

        return dialogView
    }


}