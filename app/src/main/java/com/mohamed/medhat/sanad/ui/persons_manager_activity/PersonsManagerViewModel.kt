package com.mohamed.medhat.sanad.ui.persons_manager_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.KnownPerson
import com.mohamed.medhat.sanad.networking.FakeApi
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [PersonsManagerActivity].
 */
class PersonsManagerViewModel @Inject constructor(
    val webApi: WebApi,
    val fakeApi: FakeApi
) :
    BaseViewModel() {

    private val _knownPersons = MutableLiveData<List<KnownPerson>>()
    val knownPersons: LiveData<List<KnownPerson>>
        get() = _knownPersons

    /**
     * Starts fetching the known persons of a specific blind.
     * @param blindMiniProfile The profile of the blind we want to fetch his/her known persons.
     */
    fun loadKnownPersons(blindMiniProfile: BlindMiniProfile) {
        viewModelScope.launch {
            // TODO handle loading state and error states.
            // TODO use the real api
            val knownPersonsResponse = fakeApi.getKnownPersons(blindMiniProfile.userName)
            if (knownPersonsResponse.isSuccessful) {
                _knownPersons.postValue(knownPersonsResponse.body())
            } else {
                // TODO handle error cases.
            }
        }
    }
}