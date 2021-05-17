package com.mohamed.medhat.sanad.ui.splash_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.MentorProfile
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.model.error.SingleLineError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.on_boarding_activity.OnBoardingActivity
import com.mohamed.medhat.sanad.ui.q_r_activity.QRActivity
import com.mohamed.medhat.sanad.utils.PREFS_IS_USER_CONFIRMED
import com.mohamed.medhat.sanad.utils.PREFS_USER_FIRST_NAME
import com.mohamed.medhat.sanad.utils.TAG_SPLASH
import com.mohamed.medhat.sanad.utils.managers.TOKEN
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A [ViewModel] that decides which activity should [SplashActivity] navigate to based on some conditions.
 */
class SplashNavViewModel @Inject constructor(val webApi: WebApi, val sharedPrefs: SharedPrefs) :
    BaseViewModel() {

    private val token: String

    init {
        _state.value = State.NORMAL
        token = sharedPrefs.read(TOKEN)
    }

    private val _destination = MutableLiveData<Class<*>>()

    val destination: LiveData<Class<*>?>
        get() = _destination

    /**
     * This function performs some tasks to update the [destination].
     */
    fun calculateDestination() {
        // Making sure that there is only 1 active version of this function
        if (_state.value == State.LOADING) {
            return
        }
        viewModelScope.launch {
            _state.value = State.LOADING
            // Navigate to OnBoardingActivity if it is the first time for the user to open.
            if (token.isEmpty()) {
                _destination.postValue(OnBoardingActivity::class.java)
                appError = NoError()
                _state.value = State.NORMAL
                return@launch
            }
            // A local cached confirmation state
            var isConfirmed = sharedPrefs.read(PREFS_IS_USER_CONFIRMED).toBoolean()
            // Checking the network state
            if (NetworkState.isConnected.value == true) {
                try {
                    val response = webApi.getMentorProfile()
                    if (response.isSuccessful) {
                        val profile = response.body() as MentorProfile
                        sharedPrefs.write(PREFS_USER_FIRST_NAME, profile.firstName)
                        // Setting the confirmation to the fetched value
                        isConfirmed = profile.emailConfirmed
                        // Caching the confirmation state
                        sharedPrefs.write(PREFS_IS_USER_CONFIRMED, isConfirmed.toString())
                    } else {
                        if (response.code() in 400..499 && response.code() != 408) {
                            // Unauthorized access, navigate to LoginActivity
                            _destination.postValue(LoginActivity::class.java)
                            appError = NoError()
                            _state.value = State.NORMAL
                            return@launch
                        } else {
                            Log.e(
                                TAG_SPLASH,
                                "Something went wrong while choosing a destination: ${response.code()} ${response.message()}"
                            )
                            appError =
                                SingleLineError("Something went wrong! Please wait while retrying...")
                            _state.value = State.ERROR
                            // TODO stop the infinite recursion loop?
                            calculateDestination()
                            return@launch
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    appError = SingleLineError(e.message.toString())
                    _state.postValue(State.ERROR)
                }
                // Connecting to the internet to fetch the user profile
            } else {
                // No Internet connection state
                Log.e(TAG_SPLASH, "No Internet Connection!")
                _destination.postValue(null)
                appError =
                    SingleLineError("No Internet Connection! Please connect to the internet to continue!")
                _state.value = State.ERROR
                return@launch
            }
            try {
                // Navigate to MainActivity if the user is confirmed
                if (token.isNotEmpty() && isConfirmed) {
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
                        Log.e(
                            TAG_SPLASH,
                            "Something went wrong while choosing a destination: ${blindsResponse.code()} ${blindsResponse.message()}"
                        )
                        appError =
                            SingleLineError("Something went wrong! Please wait while retrying...")
                        _state.value = State.ERROR
                        // TODO stop the infinite recursion loop?
                        calculateDestination()
                        return@launch
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                appError = SingleLineError(e.message.toString())
                _state.postValue(State.ERROR)
            }
            // Navigate to LoginActivity if the user is confirmed
            if (token.isNotEmpty() && !isConfirmed) {
                _destination.postValue(LoginActivity::class.java)
                _state.value = State.NORMAL
                return@launch
            }
        }
    }
}