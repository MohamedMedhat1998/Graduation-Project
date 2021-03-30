package com.mohamed.medhat.sanad.ui.splash_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.MentorProfile
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.confirmation_activity.ConfirmationActivity
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.on_boarding_activity.OnBoardingActivity
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

    private var isIsConfirmedCalculated = false
    private var isConfirmed = false

    /**
     * This function performs some tasks to update the [destination].
     */
    fun calculateDestination() {
        _state.value = State.LOADING
        var isError = false
        viewModelScope.launch {
            when {
                shouldGoToOnBoardingActivity() -> {
                    _destination.postValue(OnBoardingActivity::class.java)
                }
                shouldGoToConfirmationActivity() -> {
                    _destination.postValue(ConfirmationActivity::class.java)
                }
                shouldGoToMainActivity() -> {
                    _destination.postValue(MainActivity::class.java)
                }
                else -> {
                    isError = true
                    _destination.postValue(null)
                    _state.postValue(State.ERROR)
                }
            }
            if (!isError) {
                _state.value = State.NORMAL
            }
        }
    }

    /**
     * Decides whether to navigate to [OnBoardingActivity] or not.
     */
    private fun shouldGoToOnBoardingActivity(): Boolean {
        return token.isEmpty()
    }

    /**
     * Decides whether to navigate to [OnBoardingActivity] or not.
     */
    private suspend fun shouldGoToConfirmationActivity(): Boolean {
        val isConfirmed = isUserConfirmed()
        return token.isNotEmpty() && !isConfirmed
    }

    /**
     * Decides whether to navigate to [OnBoardingActivity] or not.
     */
    private suspend fun shouldGoToMainActivity(): Boolean {
        val isConfirmed = isUserConfirmed()
        return token.isNotEmpty() && isConfirmed
    }

    /**
     * @return `true` if the mentor profile is verified, `false` otherwise.
     */
    private suspend fun isUserConfirmed(): Boolean {
        if (isIsConfirmedCalculated) {
            return isConfirmed
        } else {
            val response = webApi.getMentorProfile()
            if (response.isSuccessful) {
                val mentorProfile = response.body() as MentorProfile
                isIsConfirmedCalculated = true
                Log.d("NavViewModel", "the end of the coroutine")
                isConfirmed = mentorProfile.emailConfirmed
            } else {
                // TODO handle this error state
                throw Exception("Error while fetching mentor profile ${response.code()}")
            }
        }
        return isConfirmed
    }

}