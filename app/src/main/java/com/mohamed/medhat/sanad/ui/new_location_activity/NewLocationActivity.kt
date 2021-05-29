package com.mohamed.medhat.sanad.ui.new_location_activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_new_location.*
import javax.inject.Inject

/**
 * An activity that enables the user to enter the data of the new favorite location to add.
 */
class NewLocationActivity : BaseActivity(), NewLocationView {

    @Inject
    lateinit var newLocationPresenter: NewLocationPresenter

    private val newLocationViewModel: NewLocationViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.newLocationViewModel())
            .get(NewLocationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_location)
        activityComponent.inject(this)
        newLocationPresenter.setView(this)
        newLocationPresenter.setViewModel(newLocationViewModel)
        newLocationPresenter.start(savedInstanceState)
        btn_new_place_next.setOnClickListener {
            newLocationPresenter.onNextClicked()
        }
    }

    override fun getPlaceName(): String {
        return et_new_place_name.text.toString()
    }

    override fun getAssociatedPersonName(): String {
        return et_new_place_associated_name.text.toString()
    }

    override fun getAssociatedPersonNumber(): String {
        return et_new_place_associated_number.text.toString()
    }

    override fun getPLaceDescription(): String {
        return et_new_place_description.text.toString()
    }

    override fun showLoadingIndicator() {
        pb_new_location_loading.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_new_location_loading.visibility = View.INVISIBLE
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }
}