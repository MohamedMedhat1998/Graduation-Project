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
import com.mohamed.medhat.sanad.networking.FakeApi
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the main screen.
 */
@ActivityScope
class MainViewModel @Inject constructor(val webApi: WebApi, val fakeApi: FakeApi) :
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

    private val _shouldReLogin = MutableLiveData<Boolean>()
    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    // TODO create an endpoint for that.
    fun updatePositions(blindMiniProfiles: List<BlindMiniProfile>) {
        viewModelScope.launch {
            val positionsMap = mutableMapOf<BlindMiniProfile, GpsNode?>()
            blindMiniProfiles.forEach {
                // TODO handle error cases
                // TODO use [WebApi] instead of [FakeApi].
                val gpsNode = fakeApi.getLastNode(it.userName).body()
                positionsMap[it] = gpsNode
            }
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
                    in 500..511 -> {
                        appError = SimpleConnectionError("Something went wrong!", "Server error")
                        _state.postValue(State.ERROR)
                    }
                }
                Log.d("Main", "Something went wrong while retrieving blind profiles!")
            }
        }
    }
}