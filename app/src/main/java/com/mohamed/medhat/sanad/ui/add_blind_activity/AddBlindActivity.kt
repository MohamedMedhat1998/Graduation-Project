package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
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
        btn_add_blind_add_picture.setOnClickListener {
            addBlindPresenter.onAddPictureClicked()
        }
        btn_add_blind_add_illness.setOnClickListener {
            addBlindPresenter.onAddIllnessClicked()
        }
        btn_add_blind_next.setOnClickListener {
            addBlindPresenter.onNextClicked()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        addBlindPresenter.handleOnActivityResult(requestCode, resultCode, data)
    }

    override fun updateProfilePreviewImage(uriImage: Uri?) {
        iv_add_blind_profile_preview.setImageURI(uriImage)
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

    override fun getGender(): Int {
        return if (rb_add_blind_male.isChecked) GENDER_MALE else GENDER_FEMALE
    }

    override fun getBloodType(): String {
        return sp_add_blind_blood_type.selectedItem.toString()
    }

    override fun getIllnesses(): List<String> {
        return (rv_add_blind_illnesses.adapter as IllnessesAdapter).getCheckedIllnesses()
    }
}