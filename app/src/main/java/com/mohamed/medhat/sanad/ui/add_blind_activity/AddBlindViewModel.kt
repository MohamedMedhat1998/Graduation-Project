package com.mohamed.medhat.sanad.ui.add_blind_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.model.BlindPost
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A [ViewModel] for [AddBlindActivity]
 */
class AddBlindViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {
    /**
     * Registers a new blind to the server's database.
     * @param blindPost A data object containing all the attributes of the blind to register.
     */
    fun addBlind(blindPost: BlindPost) {
        viewModelScope.launch {
            webApi.addBlind(
                blindPost.firstName,
                blindPost.lastName,
                blindPost.gender,
                blindPost.age,
                blindPost.phoneNumber,
                blindPost.serialNumber,
                blindPost.emergencyPhoneNumber,
                blindPost.bloodType,
                blindPost.illnesses,
                blindPost.profilePicture
            )
        }
    }
}