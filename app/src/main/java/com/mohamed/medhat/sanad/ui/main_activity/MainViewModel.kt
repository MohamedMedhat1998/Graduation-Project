package com.mohamed.medhat.sanad.ui.main_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.GpsNode
import com.mohamed.medhat.sanad.networking.FakeApi
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the main screen.
 */
@ActivityScope
class MainViewModel @Inject constructor(val webApi: WebApi, val fakeApi: FakeApi) :
    BaseViewModel() {

    private val _blinds = MutableLiveData<List<BlindMiniProfile>>()
    val blinds: LiveData<List<BlindMiniProfile>>
        get() = _blinds

    private val _positions = MutableLiveData<Map<BlindMiniProfile, GpsNode?>>()
    val position: LiveData<Map<BlindMiniProfile, GpsNode?>>
        get() = _positions

    init {
        viewModelScope.launch {
            val blindListResponse = webApi.getBlinds()
            if (blindListResponse.isSuccessful) {
                _blinds.postValue(blindListResponse.body())
            } else {
                // TODO handle error cases
                Log.d("Main", "Something went wrong while retrieving blind profiles!")
            }
        }
    }

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
}