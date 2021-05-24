package com.mohamed.medhat.sanad.ui.add_person_activity.pictures

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.mohamed.medhat.sanad.R
import kotlinx.android.synthetic.main.fragment_picture_preview.view.*

/**
 * A [DialogFragment] that displays an image as a dialog for previewing purpose.
 */
class PicturePreview(val picture: Uri) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.fragment_picture_preview, container, false)
        Glide.with(this).load(picture).circleCrop().into(view.iv_picture_preview_picture)
        return view
    }
}