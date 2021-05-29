package com.mohamed.medhat.sanad.ui.new_location_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.FavoritePlacePost
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.parsers.ApplicationConnectionErrorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [NewLocationActivity].
 */
@ActivityScope
class NewLocationViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {

    private val _locationAddSuccess = MutableLiveData<Boolean>()
    val locationAddSuccess: LiveData<Boolean>
        get() = _locationAddSuccess

    private val _shouldReLogin = MutableLiveData<Boolean>()
    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    /**
     * Registers a new favorite place for the blind.
     * @param blindMiniProfile The profile of the blind we want to register a new favorite location for.
     * @param favoritePlacePost An object that contains the details about the new location to add.
     */
    fun addLocation(blindMiniProfile: BlindMiniProfile, favoritePlacePost: FavoritePlacePost) {
        if (_state.value == State.LOADING) {
            // Making sure that only one instance of this function is running.
            return
        }
        if (NetworkState.isConnected.value == false) {
            appError = SimpleConnectionError(
                "No Internet Connection",
                "Please connect to the internet and try again."
            )
            _state.value = State.ERROR
            return
        }
        viewModelScope.launch {
            _state.postValue(State.LOADING)
            try {
                val response = webApi.addFavoritePlace(blindMiniProfile.userName, favoritePlacePost)
                if (response.isSuccessful) {
                    _locationAddSuccess.postValue(true)
                    _state.postValue(State.NORMAL)
                } else {
                    _locationAddSuccess.postValue(false)
                    when (response.code()) {
                        400 -> { // Application Error, parse the error and display it to the user.
                            withContext(Dispatchers.IO) {
                                appError = ApplicationConnectionErrorParser.parse(
                                    response.errorBody()?.string().toString()
                                )
                            }
                        }
                        401 -> { // Unauthorized, navigate to LoginActivity.
                            _shouldReLogin.postValue(true)
                        }
                        408 -> { // Timed out, tell the user to retry.
                            appError =
                                SimpleConnectionError("Connection Timed Out", "Please try again!")
                        }
                        in 500..511 -> { // Internal Server Error, tell the user to retry.
                            appError =
                                SimpleConnectionError("Internal Server Error", "Please try again!")
                        }
                        else -> { // Unknown Error, tell the user.
                            appError =
                                SimpleConnectionError("Unknown Error", "Something went wrong!")
                        }
                    }
                    _state.postValue(State.ERROR)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                appError = SimpleConnectionError("Unknown Error", "Something went wrong!")
                _state.value = State.ERROR
            }
        }
    }
}