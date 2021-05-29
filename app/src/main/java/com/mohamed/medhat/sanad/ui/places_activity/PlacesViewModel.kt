package com.mohamed.medhat.sanad.ui.places_activity

import android.util.Log
import androidx.lifecycle.*
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.FavoritePlace
import com.mohamed.medhat.sanad.model.GpsNode
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.FakeApi
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.TAG_PLACES
import com.mohamed.medhat.sanad.utils.parsers.ApplicationConnectionErrorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [PlacesActivity].
 */
@ActivityScope
class PlacesViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {

    private val _blindLocation = MutableLiveData<GpsNode>()
    val blindLocation: LiveData<GpsNode>
        get() = _blindLocation

    private val _favoritePlaces = MutableLiveData<List<FavoritePlace>>()
    val favoritePlaces: LiveData<List<FavoritePlace>>
        get() = _favoritePlaces

    private val _shouldReLogin = MutableLiveData<Boolean>()
    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    var hasInternetConnection = true

    private val networkObserver = Observer<Boolean> {
        hasInternetConnection = it
    }

    init {
        NetworkState.isConnected.observeForever(networkObserver)
    }

    override fun onCleared() {
        super.onCleared()
        NetworkState.isConnected.removeObserver(networkObserver)
    }

    /**
     * Fetches the last known location of the blind.
     * @param blindMiniProfile The blind user to get his/her location.
     */
    fun updateLocation(blindMiniProfile: BlindMiniProfile) {
        if (hasInternetConnection) {
            viewModelScope.launch {
                try {
                    val locationResponse = webApi.getLastNode(blindMiniProfile.userName)
                    if (locationResponse.isSuccessful) {
                        val gpsNode: GpsNode = locationResponse.body()!!
                        _blindLocation.postValue(gpsNode)
                    } else {
                        // just log the error.
                        Log.d(
                            TAG_PLACES,
                            "Location error: ${locationResponse.code()} ${locationResponse.message()}"
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Fetches the favorite places of the blind user.
     * @param blindMiniProfile The blind user to get his/her favorite places.
     */
    fun loadFavoritePlaces(blindMiniProfile: BlindMiniProfile) {
        if (_state.value == State.LOADING) {
            // Making sure that only one instance of this function is running.
            return
        }
        if (!hasInternetConnection) { // No Internet Connection, tell the user to connect.
            appError = SimpleConnectionError(
                "No Internet Connection",
                "Please connect to the internet to continue!"
            )
            _state.postValue(State.ERROR)
            return
        }
        viewModelScope.launch {
            _state.postValue(State.LOADING)
            try {
                val placesResponse = webApi.getFavoritePlaces(blindMiniProfile.userName)
                if (placesResponse.isSuccessful) {
                    val favoritePlaces: List<FavoritePlace> = placesResponse.body()!!
                    _favoritePlaces.postValue(favoritePlaces)
                    _state.postValue(State.NORMAL)
                } else {
                    when (placesResponse.code()) {
                        400 -> { // Application Error, parse the error and display it to the user.
                            withContext(Dispatchers.IO) {
                                appError = ApplicationConnectionErrorParser.parse(
                                    placesResponse.errorBody()?.string().toString()
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
            } catch (e: Exception) { // Unknown error, tell the user.
                appError = SimpleConnectionError("Unknown Error", "Something went wrong!")
                _state.postValue(State.ERROR)
                e.printStackTrace()
            }
        }
    }
}