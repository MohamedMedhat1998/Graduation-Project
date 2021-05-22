package com.mohamed.medhat.sanad.ui.login_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.LoginUser
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
 * A ViewModel for the [LoginActivity].
 */
@ActivityScope
class LoginViewModel @Inject constructor(val api: WebApi, val tokenManager: TokenManager) :
    BaseViewModel() {

    init {
        _state.value = State.NORMAL
    }

    private val _token = MutableLiveData<Token>()

    val token: LiveData<Token>
        get() = _token

    /**
     * Sends a post request to login to an existing user.
     * @param loginUser the user to login to.
     */
    fun login(loginUser: LoginUser) {
        appError = NoError()
        _state.value = State.LOADING
        viewModelScope.launch {
            try {
                val response = api.login(loginUser)
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