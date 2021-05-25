package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.add_blind_activity.blood_type.BloodTypeAdapter
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessesAdapter
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.utils.GENDER_FEMALE
import com.mohamed.medhat.sanad.utils.GENDER_MALE
import kotlinx.android.synthetic.main.activity_add_blind.*
import javax.inject.Inject

/**
 * An activity for adding blinds for the mentor to follow.
 */
class AddBlindActivity : BaseActivity(), AddBlindView {

    @Inject
    lateinit var addBlindPresenter: AddBlindPresenter

    private val addBlindViewModel: AddBlindViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.addBlindViewModel())
            .get(AddBlindViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_blind)
        activityComponent.inject(this)
        addBlindPresenter.setView(this)
        addBlindPresenter.setViewModel(addBlindViewModel)
        addBlindPresenter.start(savedInstanceState)
        btn_add_blind_pick_from_gallery.setOnClickListener {
            addBlindPresenter.onPickFromGalleryClicked()
        }
        btn_add_blind_take_photo.setOnClickListener {
            addBlindPresenter.onTakePhotoClicked()
        }
        btn_add_blind_add_illness.setOnClickListener {
            addBlindPresenter.onAddIllnessClicked()
        }
        btn_add_blind_next.setOnClickListener {
            addBlindPresenter.onNextClicked()
        }
        iv_add_blind_add_picture.setOnClickListener {
            addBlindPresenter.onAddPictureClicked()
        }
        iv_add_blind_profile_preview.setOnClickListener {
            addBlindPresenter.onPreviewClicked()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        addBlindPresenter.handleOnActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        addBlindPresenter.handleOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun updateProfilePreviewImage(uriImage: Uri?) {
        iv_add_blind_profile_preview.setPadding(0, 0, 0, 0)
        Glide.with(this).load(uriImage).circleCrop().into(iv_add_blind_profile_preview)
    }

    override fun updateProfilePreviewImage(bitmapImage: Bitmap?) {
        Glide.with(this).load(bitmapImage).into(iv_add_blind_profile_preview)
    }

    override fun getOtherIllness(): String {
        return et_add_blind_other_illness.text.toString()
    }

    override fun clearOtherIllness() {
        et_add_blind_other_illness.setText("")
    }

    override fun getName(): String {
        return et_add_blind_name.text.toString()
    }

    override fun getAge(): Int {
        return et_add_blind_age.text.toString().toInt()
    }

    override fun getPhoneNumber(): String {
        return et_add_blind_phone.text.toString()
    }

    override fun getGender(): Int {
        return if (rb_add_blind_male.isChecked) GENDER_MALE else GENDER_FEMALE
    }

    override fun getBloodType(): String {
        return (rv_add_blind_blood_type.adapter as BloodTypeAdapter).getCheckedItem().name
    }

    override fun getIllnesses(): List<String> {
        return (rv_add_blind_illnesses.adapter as IllnessesAdapter).getCheckedIllnesses()
    }

    override fun startAddPictureAnimation() {
        add_blind_motion_layout.transitionToEnd()
    }

    override fun reverseAddPictureAnimation() {
        add_blind_motion_layout.transitionToStart()
    }

    override fun getAddPictureAnimationProgress(): Float {
        return add_blind_motion_layout.progress
    }

    override fun showLoadingIndicator() {
        pb_add_blind_loading.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_add_blind_loading.visibility = View.INVISIBLE
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }
}