package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.FeedsSource
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.SortType
import kotlinx.android.synthetic.main.dialog_sort_feeds.view.*

class DialogSortFeeds : DialogFragment() {

    interface IDialogSortFeedsClickListener {
        fun onDialogSortClick(sortTypeKind: SortType, source: FeedsSource)
    }

    private var listener: IDialogSortFeedsClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort_feeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle(R.string.dialog_title)
        listener = parentFragment as IDialogSortFeedsClickListener

        view.dialog_sort_feed_button.setOnClickListener {

            var source: FeedsSource = FeedsSource.BOTH
            var sortType: SortType = SortType.ASC

            if (view.dialog_proger_check_box.isChecked && view.dialog_habr_check_box.isChecked) {
                source = FeedsSource.BOTH
            } else
                if (view.dialog_habr_check_box.isChecked) {
                    source = FeedsSource.HABR
                } else
                    if (view.dialog_proger_check_box.isChecked) {
                        source = FeedsSource.PROGER
                    }

            when (view.sort_feeds_radio_group.checkedRadioButtonId) {
                R.id.sort_feeds_by_date_asc_radio_button -> {
                    sortType = SortType.ASC
                }
                R.id.sort_feeds_by_date_desc_radio_button -> {
                    sortType = SortType.DESC
                }
            }
            listener?.onDialogSortClick(sortType, source)
            dismiss()
        }
    }


    override fun show(manager: FragmentManager, tag: String?) {
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag)
        }
    }
}