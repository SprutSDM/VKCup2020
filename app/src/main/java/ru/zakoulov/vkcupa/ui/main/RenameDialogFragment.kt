package ru.zakoulov.vkcupa.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import ru.zakoulov.vkcupa.R

class RenameDialogFragment : DialogFragment() {

    private lateinit var butCancel: Button
    private lateinit var butOk: Button
    private lateinit var docTitle: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_rename_doc, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.shape_dialog)
        with (root) {
            butCancel = findViewById(R.id.but_cancel)
            butOk = findViewById(R.id.but_ok)
            docTitle = findViewById(R.id.doc_title)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        butCancel.setOnClickListener {
            dismiss()
        }
        val oldDocTitle = arguments?.getString(MainFragment.KEY_DOC_TITLE, "")!!
        val docId = arguments?.getInt(MainFragment.KEY_DOC_ID, 0)!!

        docTitle.setText(oldDocTitle)

        butOk.setOnClickListener {
            val intent = Intent()
            intent.putExtra(MainFragment.KEY_DOC_ID, docId)
            intent.putExtra(MainFragment.KEY_DOC_TITLE, docTitle.text.toString())
            targetFragment?.onActivityResult(MainFragment.REQUEST_RENAME_DOC, Activity.RESULT_OK, intent)
            dismiss()
        }
    }
}
