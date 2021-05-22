package com.mohamed.medhat.sanad.ui.registration_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.NewUser
import com.mohamed.medhat.sanad.model.Token
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.catchServerOrResponseError
import com.mohamed.medhat.sanad.utils.catchUnknownError
import com.mohamed.medhat.sanad.utils.managers.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A [ViewModel] for the [RegistrationActivity].
 */
@ActivityScope
class RegistrationViewModel @Inject constructor(val api: WebApi, val tokenManager: TokenManager) :
    BaseViewModel() {

    init {
        _state.value = State.NORMAL
    }

    private val _token = MutableLiveData<Token>()

    val token: LiveData<Token>
        get() = _token

    /**
     * Sends a post request to the web api to create a [NewUser].
     * @param newUser the user to be created.
     */
    fun registerNewUser(newUser: NewUser) {
        appError = NoError()
        _state.value = State.LOADING
        viewModelScope.launch {
            try {
                val response = api.register(newUser)
                if (response.isSuccessful) {
                    val token: Token = response.body()!!
                    tokenManager.save(token)
                    _token.value = token
                    appError = NoError()
                    _state.value = State.NORMAL
                } else {
                    withContext(Dispatchers.IO) {
                        appError = catchServerOrResponseError(response)
                        _state.postValue(State.ERROR)
                    }
                }
            } catch (t: Throwable) {
                appError = catchUnknownError(t)
                _state.value = State.ERROR
            }
        }
    }
}