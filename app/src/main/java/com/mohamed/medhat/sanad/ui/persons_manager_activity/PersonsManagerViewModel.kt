package com.mohamed.medhat.sanad.ui.persons_manager_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.KnownPerson
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.TAG_PERSONS
import com.mohamed.medhat.sanad.utils.parsers.ApplicationConnectionErrorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [PersonsManagerActivity].
 */
@ActivityScope
class PersonsManagerViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {

    private val _knownPersons = MutableLiveData<List<KnownPerson>>()
    val knownPersons: LiveData<List<KnownPerson>>
        get() = _knownPersons

    private val _shouldReLogin = MutableLiveData<Boolean>()
    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    /**
     * Starts fetching the known persons of a specific blind.
     * @param blindMiniProfile The profile of the blind we want to fetch his/her known persons.
     */
    fun loadKnownPersons(blindMiniProfile: BlindMiniProfile) {
        if (_state.value == State.LOADING) {
            // Making sure that only one instance of this function is running.
            return
        }
        viewModelScope.launch {
            _state.postValue(State.LOADING)
            val knownPersonsResponse = webApi.getKnownPersons(blindMiniProfile.userName)
            if (knownPersonsResponse.isSuccessful) {
                _state.postValue(State.NORMAL)
                _knownPersons.postValue(knownPersonsResponse.body())
            } else {
                when (knownPersonsResponse.code()) {
                    401 -> { // Unauthorized, re-login
                        _shouldReLogin.postValue(true)
                    }
                    408 -> { // Timed out, log the error and tell the user.
                        val timeoutMessage =
                            "Request timed out while fetching the known persons of: ${blindMiniProfile.firstName} ${blindMiniProfile.lastName}"
                        Log.e(TAG_PERSONS, timeoutMessage)
                        appError = SimpleConnectionError("Request Timed Out!", timeoutMessage)
                    }
                    in 500..511 -> { // Internal server error, log the error and tell the user.
                        val internalServerErrorMessage =
                            "Internal server error while fetching the known persons of: ${blindMiniProfile.firstName} ${blindMiniProfile.lastName}"
                        Log.e(TAG_PERSONS, internalServerErrorMessage)
                        appError = SimpleConnectionError(
                            "Internal Server Error",
                            internalServerErrorMessage
                        )
                    }
                    400 -> { // Some application error, parse it and display it to the user.
                        withContext(Dispatchers.IO) {
                            appError = ApplicationConnectionErrorParser.parse(
                                knownPersonsResponse.errorBody()?.string().toString()
                            )
                        }
                    }
                    else -> { // Something went wrong, display the error to the user.
                        appError = SimpleConnectionError(
                            "Something Went Wrong - ${knownPersonsResponse.code()}",
                            knownPersonsResponse.message()
                        )
                    }
                }
                _state.postValue(State.ERROR)
            }
        }
    }
}