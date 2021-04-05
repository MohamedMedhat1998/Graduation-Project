package com.mohamed.medhat.sanad.ui.login_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.MentorProfile
import com.mohamed.medhat.sanad.model.error.SingleLineError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.confirmation_activity.ConfirmationActivity
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.q_r_activity.QRActivity
import com.mohamed.medhat.sanad.utils.IS_MENTORING_SOMEONE
import com.mohamed.medhat.sanad.utils.LOGIN
import com.mohamed.medhat.sanad.utils.USER_FIRST_NAME
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A [ViewModel] that decides which activity should [LoginActivity] navigate to based on some conditions.
 */
class LoginNavViewModel @Inject constructor(val webApi: WebApi, val sharedPrefs: SharedPrefs) :
    BaseViewModel() {

    private val _destination = MutableLiveData<Class<*>>()

    val destination: LiveData<Class<*>>
        get() = _destination

    /**
     * This function performs some tasks to update the [destination].
     */
    fun calculateDestination() {
        if (_state.value == State.LOADING) {
            return
        }
        if (NetworkState.isConnected.value != true) {
            return
        }
        viewModelScope.launch {
            _state.value = State.LOADING
            // Connecting to the internet to fetch the mentor profile
            val response = webApi.getMentorProfile()
            if (response.isSuccessful) {
                val profile = response.body() as MentorProfile
                sharedPrefs.write(USER_FIRST_NAME, profile.firstName)
                if (profile.emailConfirmed) {
                    // If the email is confirmed, we have two choices: QRActivity or MainActivity
                    if (sharedPrefs.read(IS_MENTORING_SOMEONE).toBoolean()) {
                        // TODO find a place to update `IS_MENTORING_SOMEONE`
                        // If the user is mentoring someone already, navigate directly to the MainActivity
                        _destination.value = MainActivity::class.java
                        _state.value = State.NORMAL
                        return@launch
                    } else {
                        // If the user is not mentoring anyone, navigate to the QRActivity
                        _destination.value = QRActivity::class.java
                        _state.value = State.NORMAL
                        return@launch
                    }
                } else {
                    // If the email isn't confirmed, navigate to the confirmation screen
                    _destination.value = ConfirmationActivity::class.java
                    _state.value = State.NORMAL
                    return@launch
                }
            } else {
                if (response.code() == 401) {
                    // Unauthorized
                    _destination.value = LoginActivity::class.java
                    _state.value = State.NORMAL
                    return@launch
                } else {
                    // Internal server error, retrying...
                    Log.e(
                        LOGIN,
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