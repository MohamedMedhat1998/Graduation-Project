package com.mohamed.medhat.graduation_project.ui.confirmation_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.model.error.NoError
import com.mohamed.medhat.graduation_project.networking.WebApi
import com.mohamed.medhat.graduation_project.ui.base.BaseViewModel
import com.mohamed.medhat.graduation_project.ui.helpers.State
import com.mohamed.medhat.graduation_project.utils.catchServerOrResponseError
import com.mohamed.medhat.graduation_project.utils.catchUnknownError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A ViewModel for the [ConfirmationActivity].
 */
@ActivityScope
class ConfirmationViewModel @Inject constructor(val api: WebApi) : BaseViewModel() {

    init {
        _state.value = State.NORMAL
    }

    private val _isConfirmed = MutableLiveData(false)

    val isConfirmed: LiveData<Boolean>
        get() = _isConfirmed

    /**
     * Sends the confirmation code to the remote api to confirm the email address.
     * @param confirmationCode the confirmation code to send.
     */
    fun confirmEmail(confirmationCode: String) {
        _state.value = State.LOADING
        viewModelScope.launch {
            try {
                val response = api.confirmEmail(confirmationCode)
                if (response.isSuccessful) {
                    _isConfirmed.postValue(true)
                    appError = NoError()
                    State.NORMAL
                } else {
                    withContext(Dispatchers.IO) {
                        appError = catchServerOrResponseError(response)
                        _state.postValue(State.ERROR)
                    }
                }
            } catch (t: Throwable) {
                appError = catchUnknownError(t)
            }
        }
    }
}