package com.mohamed.medhat.sanad.ui.mentor_picture_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.confirmation_activity.ConfirmationActivity
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.q_r_activity.QRActivity
import com.mohamed.medhat.sanad.ui.splash_activity.SplashActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A ViewModel that figures out the next destination screen for the [MentorPictureActivity].
 */
class MentorPictureNavViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {

    init {
        _state.value = State.NORMAL
    }

    private val _destination = MutableLiveData<Class<*>>()
    val destination: LiveData<Class<*>>
        get() = _destination

    fun calculateDestination() {
        if (_state.value == State.LOADING) {
            // Making sure that only one instance of this function is running.
            return
        }
        if (NetworkState.isConnected.value == false) {
            _destination.value = null
            appError =
                SimpleConnectionError("No Internet Connection!", "Please connect to the internet.")
            _state.postValue(State.ERROR)
            return
        }
        _state.postValue(State.LOADING)
        viewModelScope.launch {
            val profileResponse = webApi.getMentorProfile()
            if (profileResponse.isSuccessful) {
                val mentorProfile = profileResponse.body()!!
                if (!mentorProfile.emailConfirmed) { // Email not confirmed, navigate to ConfirmationActivity.
                    _destination.postValue(ConfirmationActivity::class.java)
                    _state.postValue(State.NORMAL)
                    return@launch
                } else { // Profile picture exists and the email is confirmed, check the number of blinds.
                    val blindsListResponse = webApi.getBlinds()
                    if (blindsListResponse.isSuccessful) {
                        val blindsList = blindsListResponse.body()!!
                        if (blindsList.isEmpty()) { // Blinds list is empty, navigate to AddBlindActivity.
                            _destination.postValue(QRActivity::class.java)
                            _state.postValue(State.NORMAL)
                            return@launch
                        } else {
                            // All checks passed, navigate to MainActivity.
                            _destination.postValue(MainActivity::class.java)
                            _state.postValue(State.NORMAL)
                            return@launch
                        }
                    } else {
                        when (blindsListResponse.code()) {
                            401 -> { // Unauthorized, re-login.
                                _destination.postValue(LoginActivity::class.java)
                                _state.postValue(State.NORMAL)
                                return@launch
                            }
                            408 -> { // Timed out, retry silently.
                                calculateDestination()
                                return@launch
                            }
                            in 500..511 -> { // Internal server error, restart the app.
                                _destination.postValue(SplashActivity::class.java)
                                _state.postValue(State.NORMAL)
                                return@launch
                            }
                            else -> {
                                _destination.postValue(SplashActivity::class.java)
                                _state.postValue(State.NORMAL)
                                return@launch
                            }
                        }
                    }
                }
            } else {
                when (profileResponse.code()) {
                    401 -> { // Unauthorized, re-login.
                        _destination.postValue(LoginActivity::class.java)
                        _state.postValue(State.NORMAL)
                        return@launch
                    }
                    408 -> { // Timed out, retry silently.
                        calculateDestination()
                        return@launch
                    }
                    in 500..511 -> { // Internal server error, restart the app.
                        _destination.postValue(SplashActivity::class.java)
                        _state.postValue(State.NORMAL)
                        return@launch
                    }
                    else -> {
                        _destination.postValue(SplashActivity::class.java)
                        _state.postValue(State.NORMAL)
                        return@launch
                    }
                }
            }
        }
    }
}