package com.mohamed.medhat.sanad.ui.splash_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.local.SharedPrefs
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
import com.mohamed.medhat.sanad.utils.IS_USER_CONFIRMED
import com.mohamed.medhat.sanad.utils.SPLASH
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
            var isConfirmed = sharedPrefs.read(IS_USER_CONFIRMED).toBoolean()
            // Checking the network state
            if (NetworkState.isConnected.value == true) {
                // Connecting to the internet to fetch the user profile
                val response = webApi.getMentorProfile()
                if (response.isSuccessful) {
                    val profile = response.body() as MentorProfile
                    // Setting the confirmation to the fetched value
                    isConfirmed = profile.emailConfirmed
                    // Caching the confirmation state
                    sharedPrefs.write(IS_USER_CONFIRMED, isConfirmed.toString())
                } else {
                    if (response.code() == 401) {
                        // Unauthorized access, navigate to LoginActivity
                        _destination.postValue(LoginActivity::class.java)
                        appError = NoError()
                        _state.value = State.NORMAL
                        return@launch
                    } else {
                        Log.e(
                            SPLASH,
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
            } else {
                // No Internet connection state
                Log.e(SPLASH, "No Internet Connection!")
                _destination.postValue(null)
                appError =
                    SingleLineError("No Internet Connection! Please connect to the internet to continue!")
                _state.value = State.ERROR
                return@launch
            }
            // Navigate to MainActivity if the user is confirmed
            if (token.isNotEmpty() && isConfirmed) {
                _destination.postValue(MainActivity::class.java)
                _state.value = State.NORMAL
                return@launch
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