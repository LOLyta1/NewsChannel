package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.*
import kotlinx.android.synthetic.main.dialog_sort_feeds.view.*

class DialogSortFeeds : DialogFragment() {

    interface IDialogSortFeedsClickListener {
        fun onDialogSortClick(sortTypeKind: SortType, source: FeedsSource)
    }

    private var listener: IDialogSortFeedsClickListener? = null
    private var preference: Filters? = null
    var source: FeedsSource = FeedsSource.BOTH
    var sortType: SortType = SortType.ASC

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort_feeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle(R.string.dialog_title)
        preference = NewsPreferenceSaver().getPreference(context)
        preference?.let{
            when (it.sortType) {
                SortType.ASC -> {
                    view.sort_feeds_by_date_asc_radio_button.isChecked = true
                    view.sort_feeds_by_date_desc_radio_button.isChecked = false
                }
                SortType.DESC -> {
                    view.sort_feeds_by_date_desc_radio_button.isChecked = true
                    view.sort_feeds_by_date_asc_radio_button.isChecked = false
                }
            }
            when (it.source) {
                FeedsSource.HABR -> {
                    view.dialog_habr_check_box.isChecked = true
                    view.dialog_proger_check_box.isChecked = false
                }
                FeedsSource.PROGER -> {
                    view.dialog_proger_check_box.isChecked = true
                    view.dialog_habr_check_box.isChecked = false
                }
                FeedsSource.BOTH -> {
                    view.dialog_habr_check_box.isChecked = true
                    view.dialog_proger_check_box.isChecked = true
                }
            }

            if (it.showOnlyFav) view.dialog_proger_check_box.isChecked = true

        }

        listener = parentFragment as IDialogSortFeedsClickListener

        view.dialog_sort_feed_button.setOnClickListener {


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

    override fun onDetach() {
        super.onDetach()
        Log.d(DEBUG_LOG,"dialog - onDetach")
        preference?.source = source
        preference?.sortType = sortType
        preference?.let { it1 -> NewsPreferenceSaver().setPreference(it1, context)

    }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(DEBUG_LOG,"dialog - onDestroyView")
    }

}