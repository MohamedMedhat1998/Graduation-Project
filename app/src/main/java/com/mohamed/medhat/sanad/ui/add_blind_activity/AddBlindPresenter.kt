package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import javax.inject.Inject


private const val ADD_BLIND_PICK_PICTURE = 1

/**
 * An mvp presenter for [AddBlindActivity].
 */
class AddBlindPresenter @Inject constructor() :
    AdvancedPresenter<AddBlindView, AddBlindViewModel>() {

    private lateinit var addBlindView: AddBlindView
    private lateinit var addBlindViewModel: AddBlindViewModel

    override fun start(savedInstanceState: Bundle?) {
    }

    override fun setView(view: AddBlindView) {
        addBlindView = view
    }

    override fun setViewModel(viewModel: AddBlindViewModel) {
        addBlindViewModel = viewModel
    }

    fun onAddPictureClicked() {
        addBlindView.pickPicture(ADD_BLIND_PICK_PICTURE)
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_BLIND_PICK_PICTURE) {
            if (resultCode == RESULT_OK) {
                val imageUri = data?.data
                addBlindView.updateProfilePreviewImage(imageUri)
            } else {
                addBlindView.displayToast("Something went wrong while selecting an image!")
            }
        }
    }
}