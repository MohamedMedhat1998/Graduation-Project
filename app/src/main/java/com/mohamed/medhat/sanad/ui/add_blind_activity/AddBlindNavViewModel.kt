package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.error.SingleLineError
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.promotion_video_activity.PromotionVideoActivity
import com.mohamed.medhat.sanad.utils.TAG_ADD_BLIND
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A [ViewModel] that is responsible for selecting the suitable destination activity for
 * the [AddBlindActivity].
 */
class AddBlindNavViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {

    private val _destination = MutableLiveData<Class<*>>()

    val destination: LiveData<Class<*>>
        get() = _destination

    /**
     * Responsible for doing some calculations to get the suitable destination activity.
     */
    fun calculateDestination() {
        // Making sure that only one instance of this function is running.
        if (_state.value == State.LOADING) {
            return
        }
        _state.value = State.LOADING
        /*
        There are 2 destinations are available:
            1. MainActivity: if the user is mentoring more than 1 person.
            2. PromotionVideoActivity: if the user is mentoring less than 2 persons.
         */
        viewModelScope.launch {
            val response = webApi.getBlinds()
            if (response.isSuccessful) {
                val blindList = response.body() as List<BlindMiniProfile>
                if (blindList.size > 1) {
                    // Navigate to the MainActivity directly if the user is mentoring more than 1 person.
                    _destination.postValue(MainActivity::class.java)
                    _state.postValue(State.NORMAL)
                } else {
                    // Navigate to the PromotionVideoActivity if the user is mentoring 1 or no persons.
                    _destination.postValue(PromotionVideoActivity::class.java)
                    _state.postValue(State.NORMAL)
                }
            } else {
                when (response.code()) {
                    401 -> { // Unauthorized, navigate to LoginActivity
                        _destination.postValue(LoginActivity::class.java)
                        _state.postValue(State.NORMAL)
                    }
                    else -> {
                        Log.e(
                            TAG_ADD_BLIND,
                            "Something went wrong while choosing a destination: ${response.code()} ${response.message()}"
                        )
                        appError =
                            SingleLineError("Something went wrong! Please wait while retrying...")
                        _state.value = State.ERROR
                        // TODO stop the infinite recursion loop?
                        calculateDestination()
                    }
                }
            }
        }
    }
}