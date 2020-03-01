package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.view_model.Sort
import kotlinx.android.synthetic.main.dialog_sort_feeds.view.*

class DialogSortFeeds : DialogFragment() {

    interface IDialogSortFeedsClickListener {
        fun onDialogSortClick(filter: FeedsSource, sortKind: Sort)
    }

    private var listener: IDialogSortFeedsClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort_feeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener = parentFragment as IDialogSortFeedsClickListener



        view.dialog_sort_feed_button.setOnClickListener {
            var filter: FeedsSource = FeedsSource.BOTH
            var sort: Sort = Sort.BY_ABC_ASC

            if (view.dialog_proger_check_box.isChecked && view.dialog_habr_check_box.isChecked) {
                filter = FeedsSource.BOTH
            } else
                if (view.dialog_habr_check_box.isChecked) {
                    filter = FeedsSource.HABR
                } else
                    if (view.dialog_proger_check_box.isChecked) {
                        filter = FeedsSource.PROGER
                    }

            when (view.sort_feeds_radio_group.checkedRadioButtonId) {
                R.id.sort_feeds_by_abc_asc_radio_button -> sort = Sort.BY_ABC_ASC
                R.id.sort_feeds_by_abs_desc_radio_button -> sort = Sort.BY_ABC_DESC
                R.id.sort_feeds_by_date_asc_radio_button -> sort = Sort.BY_DATE_ASC
                R.id.sort_feeds_by_date_desc_radio_button -> sort = Sort.BY_DATE_DESC
            }
            dismiss()
            listener?.onDialogSortClick(filter, sort)
        }


    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag)
        }
    }
}