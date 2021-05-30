package com.mohamed.medhat.sanad.ui.new_location_activity

import android.app.Activity
import android.os.Bundle
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.FavoritePlacePost
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.places_activity.EXTRA_NEW_LOCATION_LAT
import com.mohamed.medhat.sanad.ui.places_activity.EXTRA_NEW_LOCATION_LONG
import com.mohamed.medhat.sanad.utils.FRAGMENT_FEATURES_BLIND_PROFILE
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_new_location.*
import javax.inject.Inject

/**
 * An mvp presenter for the [NewLocationActivity].
 */
class NewLocationPresenter @Inject constructor() :
    AdvancedPresenter<NewLocationView, NewLocationViewModel>() {

    private lateinit var newLocationView: NewLocationView
    private lateinit var newLocationViewModel: NewLocationViewModel
    private lateinit var activity: NewLocationActivity
    private var lat: Double? = null
    private var long: Double? = null
    private lateinit var blindMiniProfile: BlindMiniProfile

    override fun start(savedInstanceState: Bundle?) {
        newLocationView.displayToast("NewLocation Hello World!")
        newLocationViewModel.state.observe(activity) {
            val textErrorViewer =
                TextErrorViewer(newLocationViewModel.appError, activity.tv_new_location_error)
            newLocationView.setAppErrorViewer(textErrorViewer)
            handleLoadingState(activity, it)
        }
        newLocationViewModel.shouldReLogin.observe(activity) {
            if (it) {
                newLocationView.startActivityAsRoot(LoginActivity::class.java)
            }
        }
        newLocationViewModel.locationAddSuccess.observe(activity) {
            if (it) {
                newLocationView.displayToast("تم إضافة المكان بنجاح!")
                activity.setResult(Activity.RESULT_OK)
                activity.finish()
            } else {
                newLocationView.displayToast("حدث خطأ أثناء إضافة المكان")
                activity.setResult(Activity.RESULT_CANCELED)
            }
        }
    }

    override fun setView(view: NewLocationView) {
        newLocationView = view
        activity = view as NewLocationActivity
        lat = activity.intent.extras!!.getDouble(EXTRA_NEW_LOCATION_LAT)
        long = activity.intent.extras!!.getDouble(EXTRA_NEW_LOCATION_LONG)
        blindMiniProfile =
            activity.intent.extras!!.getSerializable(FRAGMENT_FEATURES_BLIND_PROFILE) as BlindMiniProfile
    }

    override fun setViewModel(viewModel: NewLocationViewModel) {
        newLocationViewModel = viewModel
    }

    fun onNextClicked() {
        val emptyErrorWarning = activity.getString(R.string.empty_field_warning)
        if (newLocationView.getPlaceName().isEmpty()) {
            newLocationView.showInputError(activity.et_new_place_name, emptyErrorWarning)
            return
        }
        if (newLocationView.getAssociatedPersonName().isEmpty()) {
            newLocationView.showInputError(activity.et_new_place_associated_name, emptyErrorWarning)
            return
        }
        if (newLocationView.getAssociatedPersonNumber().isEmpty()) {
            newLocationView.showInputError(
                activity.et_new_place_associated_number,
                emptyErrorWarning
            )
            return
        }
        if (newLocationView.getPLaceDescription().isEmpty()) {
            newLocationView.showInputError(activity.et_new_place_description, emptyErrorWarning)
            return
        }
        // TODO use the "Associated Person Name"
        val favoritePlacePost = FavoritePlacePost(
            name = newLocationView.getPlaceName(),
            phoneNumber = newLocationView.getAssociatedPersonNumber(),
            description = newLocationView.getPLaceDescription(),
            latitude = lat!!,
            longitude = long!!
        )
        newLocationViewModel.addLocation(blindMiniProfile, favoritePlacePost)
    }

    fun onPreviousClicked() {
        activity.onBackPressed()
    }
}