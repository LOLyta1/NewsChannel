package com.hfad.news.tsivileva.newschannel.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hfad.news.tsivileva.newschannel.R
import kotlinx.android.synthetic.main.save_dialog.view.*

class DialogSaveFile:DialogFragment(){
  interface okClickListener{
      fun onSaveFileClick(fileName:String){}
  }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.save_dialog,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.save_file_button.setOnClickListener {
            ( parentFragment as? okClickListener)?.onSaveFileClick(view.file_name_edit_text.text.toString())
            dismiss()
        }
        view.file_save_cancel_button.setOnClickListener {
            dismiss()
        }
    }
}