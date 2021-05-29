package com.mohamed.medhat.sanad.ui.mentor_picture_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.parsers.ApplicationConnectionErrorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * An mvvm ViewModel for [MentorPictureActivity].
 */
@ActivityScope
class MentorPictureViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {

    init {
        _state.value = State.NORMAL
    }

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean>
        get() = _uploadSuccess

    private val _shouldReLogin = MutableLiveData<Boolean>()
    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    fun uploadProfilePicture(multipartBodyPart: MultipartBody.Part) {
        if (_state.value == State.LOADING) {
            // Making sure that only one instance of this function is running.
            return
        }
        if (NetworkState.isConnected.value == false) { // No internet error.
            appError = SimpleConnectionError(
                "No internet connection!",
                "Please make sure that you are connected to the internet."
            )
            _state.value = State.ERROR
            return
        }
        _state.value = State.LOADING
        viewModelScope.launch {
            val response = webApi.uploadMentorPicture(multipartBodyPart)
            if (response.isSuccessful) {
                _uploadSuccess.postValue(true)
                _state.postValue(State.NORMAL)
            } else {
                _uploadSuccess.postValue(false)
                when (response.code()) {
                    401 -> { // Unauthorized, re-login.
                        _shouldReLogin.postValue(true)
                    }
                    408 -> { // Timed out, tell the user to retry.
                        appError =
                            SimpleConnectionError("Connection Timed Out!", "Please try again.")
                        _state.postValue(State.ERROR)
                    }
                    in 500..511 -> { // Internal server error, tell the user to retry.
                        appError =
                            SimpleConnectionError("Internal Server Error!", "Please try again.")
                        _state.postValue(State.ERROR)
                    }
                    400 -> { // Application error, parse it and display it to the user.
                        withContext(Dispatchers.IO) {
                            appError = ApplicationConnectionErrorParser.parse(
                                response.errorBody()?.string().toString()
                            )
                        }
                        _state.postValue(State.ERROR)
                    }
                    else -> { // Unknown error, tell the user.
                        appError = SimpleConnectionError("Unknown error!", "Something went wrong.")
                        _state.postValue(State.ERROR)
                    }
                }
            }
        }
    }
}