package ru.zakoulov.vkcupe.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import kotlin.math.max

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

fun Uri.getResizedBitmapImage(viewWidth: Int, context: Context): Bitmap? {
    var input = context.contentResolver.openInputStream(this) ?: return null

    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    options.inPreferredConfig = Bitmap.Config.ARGB_8888

    BitmapFactory.decodeStream(input, null, options)
    input.close()

    if ((options.outWidth == -1) || (options.outHeight == -1)) {
        return null
    }

    var rotatedWidth = options.outWidth
    val orientation = getOrientation(context)

    if (orientation == 90 || orientation == 270) {
        rotatedWidth = options.outHeight
    }

    val ratio: Float = max(1f, rotatedWidth.toFloat() / viewWidth)

    val bitmapOptions = BitmapFactory.Options()
    bitmapOptions.inSampleSize = Integer.highestOneBit(ratio.toInt())
    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888

    input = context.contentResolver?.openInputStream(this) ?: return null
    var bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
    input.close()

    bitmap ?: return null

    if (orientation > 0) {
        val matrix = Matrix()
        matrix.postRotate(orientation.toFloat())
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    return bitmap
}

fun Uri.getOrientation(context: Context): Int {
    val cursor = context.contentResolver.query(this,
        arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null) ?: return -1
    if (cursor.count == 0) {
        return -1
    }
    cursor.moveToFirst()
    val orientation = cursor.getInt(0)
    cursor.close()
    return orientation
}
