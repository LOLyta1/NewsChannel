package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.FeedsSource
import kotlinx.android.synthetic.main.dialog_filter_feeds.view.*


class DialogFilterFeeds : DialogFragment() {
    interface IDialogFilterFeedsListener{
        fun onFilterButtonClick(sourceKind: FeedsSource)
    }
    private lateinit var listener: IDialogFilterFeedsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable=false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_filter_feeds,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener=parentFragment as IDialogFilterFeedsListener

        view.dialog_filter_button.setOnClickListener {
            if(view.dialog_proger_check_box.isChecked){
                listener.onFilterButtonClick(FeedsSource.PROGER)
            }else{
                if(view.dialog_habr_check_box.isChecked){
                    listener.onFilterButtonClick(FeedsSource.HABR)

                }else
                    if(view.dialog_proger_check_box.isChecked && view.dialog_habr_check_box.isChecked){
                        listener.onFilterButtonClick(FeedsSource.BOTH)
                    }
            }

            dismiss()
        }
    }
    override fun show(manager: FragmentManager, tag: String?) {
        if(manager.findFragmentByTag(tag)==null){
            super.show(manager, tag)
        }
    }

}