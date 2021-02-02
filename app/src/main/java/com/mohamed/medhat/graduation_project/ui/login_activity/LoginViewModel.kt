package com.mohamed.medhat.graduation_project.ui.login_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.model.LoginUser
import com.mohamed.medhat.graduation_project.model.Token
import com.mohamed.medhat.graduation_project.model.error.NoError
import com.mohamed.medhat.graduation_project.model.error.SimpleConnectionError
import com.mohamed.medhat.graduation_project.networking.WebApi
import com.mohamed.medhat.graduation_project.ui.base.BaseViewModel
import com.mohamed.medhat.graduation_project.ui.helpers.State
import com.mohamed.medhat.graduation_project.utils.catchServerOrResponseError
import com.mohamed.medhat.graduation_project.utils.catchUnknownError
import com.mohamed.medhat.graduation_project.utils.parsers.ApplicationConnectionErrorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A ViewModel for the [LoginActivity].
 */
@ActivityScope
class LoginViewModel @Inject constructor(val api: WebApi) : BaseViewModel() {

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