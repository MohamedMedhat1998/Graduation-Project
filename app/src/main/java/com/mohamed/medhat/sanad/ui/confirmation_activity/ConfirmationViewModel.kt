package com.mohamed.medhat.sanad.ui.confirmation_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.error.DetailedConnectionError
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.IS_USER_CONFIRMED
import com.mohamed.medhat.sanad.utils.catchServerOrResponseError
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A ViewModel for the [ConfirmationActivity].
 */
@ActivityScope
class ConfirmationViewModel @Inject constructor(val api: WebApi, val sharedPrefs: SharedPrefs) :
    BaseViewModel() {

    init {
        _state.value = State.NORMAL
    }

    private val _isConfirmed = MutableLiveData<Boolean>()

    val isConfirmed: LiveData<Boolean>
        get() = _isConfirmed

    private val _shouldReLogin = MutableLiveData<Boolean>()

    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    /**
     * Sends the confirmation code to the remote api to confirm the email address.
     * @param confirmationCode the confirmation code to send.
     */
    fun confirmEmail(confirmationCode: String) {
        if (NetworkState.isConnected.value != true) {
            return
        }
        if (_state.value == State.LOADING) {
            return
        }
        _state.value = State.LOADING
        viewModelScope.launch {
            val response = api.confirmEmail(confirmationCode)
            if (response.isSuccessful) {
                sharedPrefs.write(IS_USER_CONFIRMED, true.toString())
                _isConfirmed.postValue(true)
                appError = NoError()
                _state.postValue(State.NORMAL)
                return@launch
            } else {
                if (response.code() in 400..499 && response.code() != 408) {
                    val error = catchServerOrResponseError(response)
                    if (error is DetailedConnectionError) {
                        if (error.title != "Invlaid Code") {
                            _shouldReLogin.postValue(true)
                        }
                    }
                    appError = error
                    _state.postValue(State.ERROR)
                    return@launch
                } else {
                    // TODO blocked on UI
                    appError =
                        SimpleConnectionError("Something went wrong!", response.message())
                    _state.postValue(State.ERROR)
                }
            }
        }
    }
}