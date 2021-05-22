package com.mohamed.medhat.sanad.ui.login_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.MentorProfile
import com.mohamed.medhat.sanad.model.error.SingleLineError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.confirmation_activity.ConfirmationActivity
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.q_r_activity.QRActivity
import com.mohamed.medhat.sanad.utils.PREFS_USER_EMAIL
import com.mohamed.medhat.sanad.utils.PREFS_USER_FIRST_NAME
import com.mohamed.medhat.sanad.utils.TAG_LOGIN
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**
 * A [ViewModel] that decides which activity should [LoginActivity] navigate to based on some conditions.
 */
@Deprecated("Use CommonNavViewModel instead.")
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
                sharedPrefs.write(PREFS_USER_FIRST_NAME, profile.firstName)
                sharedPrefs.write(PREFS_USER_EMAIL, profile.email)
                if (profile.emailConfirmed) {
                    // If the email is confirmed, we have two choices: QRActivity or MainActivity
                    val blindsResponse = webApi.getBlinds()
                    if (blindsResponse.isSuccessful) {
                        val blindsList = blindsResponse.body() as List<BlindMiniProfile>
                        if (blindsList.isEmpty()) {
                            // If the user is not mentoring anyone, navigate to the QRActivity
                            _destination.value = QRActivity::class.java
                            _state.value = State.NORMAL
                            return@launch
                        } else {
                            // If the user is mentoring someone already, navigate directly to the MainActivity
                            _destination.value = MainActivity::class.java
                            _state.value = State.NORMAL
                            return@launch
                        }
                    } else {
                        handleFailedResponse(blindsResponse)
                    }
                } else {
                    // If the email isn't confirmed, navigate to the confirmation screen
                    _destination.value = ConfirmationActivity::class.java
                    _state.value = State.NORMAL
                    return@launch
                }
            } else {
                handleFailedResponse(response)
            }
        }
    }

    /**
     * Handles the error cases that may occur if the `response.isSuccessful == false`.
     * @param response The failed response.
     */
    private fun <T> handleFailedResponse(response: Response<T>) {
        if (response.code() == 401) {
            // Unauthorized
            _destination.value = LoginActivity::class.java
            _state.value = State.NORMAL
            return
        } else {
            // Internal server error, retrying...
            Log.e(
                TAG_LOGIN,
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