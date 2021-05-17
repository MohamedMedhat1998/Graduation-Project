package com.mohamed.medhat.sanad.ui.add_person_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_person.*
import javax.inject.Inject

/**
 * An activity that allows the user to add new persons for the a blind person so that the device
 * can recognize them.
 */
class AddPersonActivity : BaseActivity(), AddPersonView {

    @Inject
    lateinit var presenter: AddPersonPresenter

    val viewModel: AddPersonViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.addPersonViewModel())
            .get(AddPersonViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)
        activityComponent.inject(this)
        presenter.setView(this)
        presenter.setViewModel(viewModel)
        presenter.start(savedInstanceState)
        btn_add_person_pick_from_gallery.setOnClickListener {
            presenter.onPickFromGalleryClicked()
        }
        btn_add_person_take_photo.setOnClickListener {
            presenter.onTakePhotoClicked()
        }
        btn_add_person_done.setOnClickListener {
            presenter.onDoneClicked()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleOnActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        presenter.handleOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun getName(): String {
        return et_add_person_name.text.toString()
    }

    override fun disablePictureButtons() {
        btn_add_person_pick_from_gallery.isEnabled = false
        btn_add_person_take_photo.isEnabled = false
    }

    override fun enablePictureButtons() {
        btn_add_person_pick_from_gallery.isEnabled = true
        btn_add_person_take_photo.isEnabled = true
    }

    override fun showLoadingIndicator() {
        pb_add_person_loading.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_add_person_loading.visibility = View.INVISIBLE
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }
}