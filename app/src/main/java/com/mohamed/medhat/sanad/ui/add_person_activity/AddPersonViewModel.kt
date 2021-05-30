package com.mohamed.medhat.sanad.ui.add_person_activity

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.KnownPersonData
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.parsers.ApplicationConnectionErrorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [AddPersonActivity].
 */
@ActivityScope
class AddPersonViewModel @Inject constructor(val webApi: WebApi) :
    BaseViewModel() {

    private val _shouldReLogin = MutableLiveData<Boolean>()
    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    private val _registrationSuccessful = MutableLiveData<Boolean>()
    val registrationSuccessful: LiveData<Boolean>
        get() = _registrationSuccessful

    /**
     * Registers a new person to the remote server.
     * @param knownPersonData The data of the new person to add.
     * @param picture A [Uri] object of the picture of the person.
     * @param blindMiniProfile The data of the blind person to add the person to.
     */
    fun addNewPerson(
        knownPersonData: KnownPersonData,
        picture: Uri,
        blindMiniProfile: BlindMiniProfile,
        context: Context
    ) {
        if (_state.value == State.LOADING) {
            // Making sure that only one instance of this function is running.
            return
        }
        // Resetting the picture errors number for the new requests.
        appError = NoError()
        viewModelScope.launch {
            // TODO move the logic to a service
            _state.postValue(State.LOADING)
            // Registering the person's data (e.g. name, reminder about, ...etc)
            val response = webApi.addKnownPerson(knownPersonData)
            if (response.isSuccessful) {
                val knownPerson = response.body()
                if (knownPerson != null) {
                    // If registering the person's data was successful, start uploading his/her pictures.
                    withContext(Dispatchers.IO) {
                        val inputStream = context.contentResolver.openInputStream(picture)
                        if (inputStream != null) {
                            val blindUsernamePart =
                                MultipartBody.Part.createFormData(
                                    "BlindUsername",
                                    blindMiniProfile.userName
                                )
                            val personIdPart =
                                MultipartBody.Part.createFormData("PersonId", knownPerson.id)
                            val pictureRequestBody =
                                inputStream.readBytes()
                                    .toRequestBody("application/octet-stream".toMediaType())
                            val picturePart = MultipartBody.Part.createFormData(
                                "File",
                                "known_person_picture_${knownPerson.id}.png",
                                pictureRequestBody
                            )
                            try {
                                val pictureResponse = webApi.addNewPictureForKnownPerson(
                                    blindUsernamePart,
                                    personIdPart,
                                    picturePart
                                )
                                if (pictureResponse.isSuccessful) {
                                    _registrationSuccessful.postValue(true)
                                    _state.postValue(State.NORMAL)
                                } else {
                                    _registrationSuccessful.postValue(false)
                                    when (pictureResponse.code()) {
                                        408 -> { // Timed out, tell the user to retry.
                                            appError = SimpleConnectionError(
                                                "Request Timed Out!",
                                                "Please try again."
                                            )
                                            _state.postValue(State.ERROR)
                                        }
                                        401 -> { // Unauthorized, re-login.
                                            _shouldReLogin.postValue(true)
                                        }
                                        in 500..511 -> { // Internal server error, tell the user to retry.
                                            appError = SimpleConnectionError(
                                                "Internal Server Error!",
                                                "Please try again later."
                                            )
                                            _state.postValue(State.ERROR)
                                        }
                                        400 -> { // Application Error, display the detailed error.
                                            withContext(Dispatchers.IO) {
                                                appError = ApplicationConnectionErrorParser.parse(
                                                    response.errorBody()?.string().toString()
                                                )
                                                _state.postValue(State.ERROR)
                                            }
                                        }
                                        else -> { // Unknown error, tell the user about it.
                                            SimpleConnectionError(
                                                "Unknown Error!",
                                                "Please try again later."
                                            )
                                            _state.postValue(State.ERROR)
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    //------------------------------------------------------------------------------------
                } else {
                    appError = SimpleConnectionError("Something went wrong!", "Please try again.")
                    _state.postValue(State.ERROR)
                }
            } else {
                // If there was a problem while registering the user, display the suitable error.
                when (response.code()) {
                    408 -> { // Timed out, tell the user to retry.
                        appError = SimpleConnectionError("Request Timed Out!", "Please try again.")
                        _state.postValue(State.ERROR)
                    }
                    401 -> { // Unauthorized, re-login.
                        _shouldReLogin.postValue(true)
                    }
                    in 500..511 -> { // Internal server error, tell the user to retry.
                        appError = SimpleConnectionError(
                            "Internal Server Error!",
                            "Please try again later."
                        )
                        _state.postValue(State.ERROR)
                    }
                    400 -> { // Application Error, display the detailed error.
                        withContext(Dispatchers.IO) {
                            appError = ApplicationConnectionErrorParser.parse(
                                response.errorBody()?.string().toString()
                            )
                            _state.postValue(State.ERROR)
                        }
                    }
                    else -> { // Unknown error, tell the user about it.
                        SimpleConnectionError("Unknown Error!", "Please try again later.")
                        _state.postValue(State.ERROR)
                    }
                }
            }
        }
    }
}