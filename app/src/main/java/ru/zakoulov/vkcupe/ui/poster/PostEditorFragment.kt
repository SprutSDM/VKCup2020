package ru.zakoulov.vkcupe.ui.poster

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupe.R
import kotlin.math.max

class PostEditorFragment : BottomSheetDialogFragment() {

    private lateinit var postImage: ImageView

    override fun getTheme() = R.style.VkCupTheme_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_post_editor, container, false)
        with (root) {
            postImage = findViewById(R.id.image_post)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUri = arguments?.getString(KEY_IMAGE_URI) ?: return
        postImage.clipToOutline = true
        postImage.viewTreeObserver.addOnGlobalLayoutListener( object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                postImage.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (context != null) {
                    postImage.setImageBitmap(getResizedBitmapImage(Uri.parse(imageUri), postImage.width))
                }
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.behavior.skipCollapsed = true
        bottomSheetDialog.setOnShowListener {
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    private fun getResizedBitmapImage(uri: Uri, viewWidth: Int): Bitmap? {
        var input = requireContext().contentResolver.openInputStream(uri) ?: return null

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        BitmapFactory.decodeStream(input, null, options)
        input.close()

        if ((options.outWidth == -1) || (options.outHeight == -1)) {
            return null
        }

        var rotatedWidth = options.outWidth
        val orientation = getOrientation(uri)

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = options.outHeight
        }

        val ratio: Float = max(1f, rotatedWidth.toFloat() / viewWidth)

        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = Integer.highestOneBit(ratio.toInt())
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888

        input = context?.contentResolver?.openInputStream(uri) ?: return null
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

    private fun getOrientation(uri: Uri): Int {
        val cursor = requireContext().contentResolver.query(uri,
            arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null) ?: return -1
        if (cursor.count == 0) {
            return -1
        }
        cursor.moveToFirst()
        val orientation = cursor.getInt(0)
        cursor.close()
        return orientation
    }

    companion object {
        const val KEY_IMAGE_URI = "key_image_uri"
    }
}
