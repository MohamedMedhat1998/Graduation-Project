package com.mohamed.medhat.sanad.ui.mentor_picture_activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mentor_picture.*
import javax.inject.Inject

/**
 * An activity that allows the mentor to upload a profile picture.
 */
class MentorPictureActivity : BaseActivity(), MentorPictureView {

    @Inject
    lateinit var mentorPicturePresenter: MentorPicturePresenter
    private val mentorPictureViewModel: MentorPictureViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.mentorPictureViewModel())
            .get(MentorPictureViewModel::class.java)
    }
    private val mentorPictureNavViewModel: MentorPictureNavViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.mentorPictureNavViewModel())
            .get(MentorPictureNavViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_picture)
        activityComponent.inject(this)
        mentorPicturePresenter.setView(this)
        mentorPicturePresenter.setViewModel(mentorPictureViewModel)
        mentorPicturePresenter.setNavViewModel(mentorPictureNavViewModel)
        mentorPicturePresenter.start(savedInstanceState)
        btn_mentor_picture_take_photo.setOnClickListener {
            mentorPicturePresenter.onTakePhotoClicked()
        }
        btn_mentor_picture_pick_from_gallery.setOnClickListener {
            mentorPicturePresenter.onPickFromGalleryClicked()
        }
        btn_mentor_picture_next.setOnClickListener {
            mentorPicturePresenter.onNextClicked()
        }
    }

    override fun updateProfilePicturePreview(image: Uri?) {
        Glide.with(this)
            .load(image)
            .circleCrop()
            .into(iv_mentor_picture_preview)
    }

    override fun showLoadingIndicator() {
        pb_mentor_picture_loading_indicator.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_mentor_picture_loading_indicator.visibility = View.INVISIBLE
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        mentorPicturePresenter.handleOnRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mentorPicturePresenter.handleOnActivityResult(requestCode, resultCode, data)
    }
}