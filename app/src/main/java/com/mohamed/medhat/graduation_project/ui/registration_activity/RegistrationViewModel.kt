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
import com.mohamed.medhat.graduation_project.utils.REPORT_ERROR
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A [ViewModel] for the [RegistrationActivity].
 */
@ActivityScope
class RegistrationViewModel @Inject constructor(val api: WebApi) : ViewModel() {

    private val _token = MutableLiveData<Token>()

    val token: LiveData<Token>
        get() = _token

    fun registerNewUser(newUser: NewUser) {
        viewModelScope.launch {
            try {
                val token: Token = api.register(newUser)
                _token.value = token
            } catch (e: Exception) {
                Log.e(REPORT_ERROR, "Invalid token received")
                e.printStackTrace()
            }
        }
    }
}