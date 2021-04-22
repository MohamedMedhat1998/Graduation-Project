package com.mohamed.medhat.sanad.ui.add_blind_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.model.BlindPost
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.parsers.ApplicationConnectionErrorParser
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A [ViewModel] for [AddBlindActivity]
 */
class AddBlindViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {

    init {
        _state.value = State.NORMAL
    }

    private val _shouldReLogin = MutableLiveData<Boolean>()

    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    private val _isSuccessfulRegistration = MutableLiveData<Boolean>()

    val isSuccessfulRegistration: LiveData<Boolean>
        get() = _isSuccessfulRegistration

    /**
     * Registers a new blind to the server's database.
     * @param blindPost A data object containing all the attributes of the blind to register.
     */
    fun addBlind(blindPost: BlindPost) {
        viewModelScope.launch {
            appError = NoError()
            _state.postValue(State.LOADING)
            val response = webApi.addBlind(
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
            if (response.isSuccessful) {
                appError = NoError()
                _isSuccessfulRegistration.postValue(true)
                _state.postValue(State.NORMAL)
            } else {
                _isSuccessfulRegistration.postValue(false)
                when (response.code()) {
                    400 -> {
                        withContext(IO) {
                            appError = ApplicationConnectionErrorParser.parse(
                                response.errorBody()?.string().toString()
                            )
                        }
                    }
                    500 -> {
                        appError = SimpleConnectionError(
                            "Something went wrong!",
                            response.message()
                        )
                    }
                    401 -> {
                        _shouldReLogin.postValue(true)
                    }
                }
                _state.postValue(State.ERROR)
            }
        }
    }
}