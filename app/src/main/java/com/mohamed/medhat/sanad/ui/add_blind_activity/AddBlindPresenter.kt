package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessItem
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessesAdapter
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import kotlinx.android.synthetic.main.activity_add_blind.*
import javax.inject.Inject


private const val ADD_BLIND_PICK_PICTURE = 1

/**
 * An mvp presenter for [AddBlindActivity].
 */
class AddBlindPresenter @Inject constructor(val illnessesAdapter: IllnessesAdapter) :
    AdvancedPresenter<AddBlindView, AddBlindViewModel>() {

    private lateinit var addBlindView: AddBlindView
    private lateinit var addBlindViewModel: AddBlindViewModel
    private lateinit var addBlindActivity: AddBlindActivity
    private lateinit var rvIllnesses: RecyclerView

    override fun start(savedInstanceState: Bundle?) {
        rvIllnesses = addBlindActivity.rv_add_blind_illnesses
        rvIllnesses.layoutManager = LinearLayoutManager(addBlindActivity)
        rvIllnesses.adapter = illnessesAdapter
    }

    override fun setView(view: AddBlindView) {
        addBlindView = view
        addBlindActivity = view as AddBlindActivity
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

    fun onAddIllnessClicked() {
        // TODO handle empty illness.
        illnessesAdapter.addIllness(IllnessItem(addBlindView.getOtherIllness(), true))
        rvIllnesses.smoothScrollToPosition(illnessesAdapter.itemCount - 1)
        addBlindView.clearOtherIllness()
    }
}