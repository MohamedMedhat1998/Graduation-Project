package com.mohamed.medhat.graduation_project.ui.registration_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.model.NewUser
import com.mohamed.medhat.graduation_project.model.Token
import com.mohamed.medhat.graduation_project.networking.WebApi
import com.mohamed.medhat.graduation_project.ui.base.BaseViewModel
import com.mohamed.medhat.graduation_project.ui.helpers.State
import com.mohamed.medhat.graduation_project.utils.REPORT_ERROR
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A [ViewModel] for the [RegistrationActivity].
 */
@ActivityScope
class RegistrationViewModel @Inject constructor(val api: WebApi) : BaseViewModel() {

    init {
        _state.value = State.NORMAL
    }

    private val _token = MutableLiveData<Token>()

    val token: LiveData<Token>
        get() = _token

    var error = ""

    /**
     * Sends a post request to the web api to create a [NewUser].
     * @param newUser the user to be created.
     */
    fun registerNewUser(newUser: NewUser) {
        error = ""
        _state.value = State.LOADING
        viewModelScope.launch {
            try {
                // TODO handle the response error by providing a model class for the error based on the schema from the web
                /*val body = api.register(newUser).body()
                val raw = api.register(newUser).raw()
                Log.d("Body", body.toString())
                Log.d("Raw", raw.toString())
                val token: Token = api.register(newUser).body()!!
                _token.value = token
                _state.value = State.NORMAL*/
                val response = api.register(newUser)
                if (response.isSuccessful) {
                    Log.d("isSuccessful?", true.toString())
                    val token: Token = response.body()!!
                    _token.value = token
                    error = ""
                    _state.value = State.NORMAL
                } else {
                    Log.d("isSuccessful?", false.toString())
                    // TODO handle the errors
                    Log.d(REPORT_ERROR, response.message())
                    error = response.message()
                    _state.value = State.ERROR
                }
            } catch (t: Throwable) {
                // TODO handle the other errors like "no internet connection" after you receive the unified error schema from the web
                Log.e(REPORT_ERROR, "Invalid token received")
                t.printStackTrace()
                Log.e("cause", t.cause.toString())
                Log.e("message", t.message.toString())
                error = t.message.toString()
                _state.value = State.ERROR
            }
        }
    }
}