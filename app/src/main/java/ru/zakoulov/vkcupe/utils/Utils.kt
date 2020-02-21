package ru.zakoulov.vkcupe.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment

fun DialogFragment.hideKeyboard() {
    val view = dialog?.currentFocus
    view?.let { v ->
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun Uri.getAbsolutePathUri(context: Context): Uri {
    if (this.scheme == "file") {
        if (this.path != null) return this
        return this
    }
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(this, proj, null, null, null)
    val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    return Uri.parse("file://" + cursor.getString(columnIndex))
}
