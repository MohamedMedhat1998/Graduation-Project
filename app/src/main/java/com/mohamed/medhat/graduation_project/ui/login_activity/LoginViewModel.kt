package com.mohamed.medhat.graduation_project.ui.login_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.model.LoginUser
import com.mohamed.medhat.graduation_project.model.Token
import com.mohamed.medhat.graduation_project.networking.WebApi
import com.mohamed.medhat.graduation_project.ui.base.BaseViewModel
import com.mohamed.medhat.graduation_project.ui.helpers.State
import kotlinx.coroutines.launch
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
        // TODO handle the errors
        _state.value = State.LOADING
        viewModelScope.launch {
            try {
                val response = api.login(loginUser)
                if (response.isSuccessful) {
                    val token: Token = response.body()!!
                    _token.value = token
                    error = ""
                    _state.value = State.NORMAL
                } else {
                    error = response.message()
                    _state.value = State.ERROR
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                error = t.message.toString()
                _state.value = State.ERROR
            }
        }
    }
}