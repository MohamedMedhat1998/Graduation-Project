package com.mohamed.medhat.sanad.ui.main_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.GpsNode
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.model.gps_errors.GpsError
import com.mohamed.medhat.sanad.model.gps_errors.GpsNoLocationError
import com.mohamed.medhat.sanad.model.gps_errors.GpsTrackingError
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.TAG_MAIN
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the main screen.
 */
@ActivityScope
class MainViewModel @Inject constructor(val webApi: WebApi) :
    BaseViewModel() {

    init {
        _state.value = State.NORMAL
        appError = NoError()
    }

    private val _blinds = MutableLiveData<List<BlindMiniProfile>>()
    val blinds: LiveData<List<BlindMiniProfile>>
        get() = _blinds

    private val _positions = MutableLiveData<Map<BlindMiniProfile, GpsNode?>>()
    val position: LiveData<Map<BlindMiniProfile, GpsNode?>>
        get() = _positions

    private val _gpsErrors = MutableLiveData<Map<String, GpsError>>()
    val gpsErrors: LiveData<Map<String, GpsError>>
        get() = _gpsErrors

    private val _shouldReLogin = MutableLiveData<Boolean>()
    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    var canUpdate = true

    /**
     * Fetches the gps coordinates of the followed blinds list.
     * @param blindMiniProfiles The list of the blinds to get their locations.
     */
    fun updatePositions(blindMiniProfiles: List<BlindMiniProfile>) {
        viewModelScope.launch {
            val positionsMap = mutableMapOf<BlindMiniProfile, GpsNode?>()
            val gpsErrorsMap = mutableMapOf<String, GpsError>()
            blindMiniProfiles.forEach {
                if (!canUpdate) {
                    return@launch
                }
                try {
                    val response = webApi.getLastNode(it.userName)
                    if (response.isSuccessful) {
                        val gpsNode = response.body()
                        positionsMap[it] = gpsNode
                        gpsErrorsMap.remove(it.userName)
                    } else {
                        when (response.code()) {
                            401 -> { // Unauthorized, navigate to login screen.
                                _shouldReLogin.postValue(true)
                            }
                            408 -> { // Timed out, log the error but don't tell the user.
                                Log.e(
                                    TAG_MAIN,
                                    "Request timed out while fetching the position of: ${it.firstName} ${it.lastName}"
                                )
                            }
                            in 500..511 -> { // Internal server error, log the error but don't tell the user.
                                Log.e(
                                    TAG_MAIN,
                                    "Internal server error while fetching the position of: ${it.firstName} ${it.lastName}"
                                )
                            }
                            422 -> { // Tracking disabled
                                gpsErrorsMap[it.userName] = GpsTrackingError()
                                _gpsErrors.postValue(gpsErrorsMap)
                            }
                            404 -> { // No locations for this device
                                gpsErrorsMap[it.userName] = GpsNoLocationError()
                                _gpsErrors.postValue(gpsErrorsMap)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            _gpsErrors.postValue(gpsErrorsMap)
            _positions.postValue(positionsMap)
        }
    }

    /**
     * Fetches the blind profiles from the remote server.
     */
    fun reloadBlindProfiles() {
        // making sure that only one instance of this function is running.
        if (_state.value == State.LOADING) {
            return
        }
        viewModelScope.launch {
            _state.postValue(State.LOADING)
            val blindListResponse = webApi.getBlinds()
            if (blindListResponse.isSuccessful) {
                _blinds.postValue(blindListResponse.body())
                appError = NoError()
                _state.value = State.NORMAL
            } else {
                when (blindListResponse.code()) {
                    401 -> { // Unauthorized, navigate to login.
                        _shouldReLogin.postValue(true)
                    }
                    408 -> { // Timed out, tell the user about that.
                        appError = SimpleConnectionError(
                            "Request timed out!",
                            "Please restart the application!"
                        )
                        _state.postValue(State.ERROR)
                    }
                    in 500..511 -> { // Internal server error, tell the user about that.
                        appError = SimpleConnectionError("Something went wrong!", "Server error")
                        _state.postValue(State.ERROR)
                    }
                }
                Log.d(TAG_MAIN, "Something went wrong while retrieving blind profiles!")
            }
        }
    }
}