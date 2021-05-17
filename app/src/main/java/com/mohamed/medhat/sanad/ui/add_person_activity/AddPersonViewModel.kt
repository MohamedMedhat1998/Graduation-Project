package com.mohamed.medhat.sanad.ui.add_person_activity

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.KnownPersonData
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.TAG_PERSONS
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
class AddPersonViewModel @Inject constructor(val webApi: WebApi) :
    BaseViewModel() {

    private val _shouldReLogin = MutableLiveData<Boolean>()
    val shouldReLogin: LiveData<Boolean>
        get() = _shouldReLogin

    private val _registrationSuccessful = MutableLiveData<Pair<Boolean, Int>>()
    val registrationSuccessful: LiveData<Pair<Boolean, Int>>
        get() = _registrationSuccessful

    private var pictureErrors = 0

    /**
     * Registers a new person to the remote server.
     * @param knownPersonData The data of the new person to add.
     * @param pictures A [Uri] list containing the [Uri] objects of the pictures of the person.
     * @param blindMiniProfile The data of the blind person to add the person to.
     */
    fun addNewPerson(
        knownPersonData: KnownPersonData,
        pictures: List<Uri>,
        blindMiniProfile: BlindMiniProfile,
        context: Context
    ) {
        if (_state.value == State.LOADING) {
            // Making sure that only one instance of this function is running.
            return
        }
        // Resetting the picture errors number for the new requests.
        pictureErrors = 0
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
                    pictures.forEachIndexed { index, it ->
                        val inputStream = context.contentResolver.openInputStream(it)
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
                                "known_person_picture_${index + 1}.png",
                                pictureRequestBody
                            )
                            try {
                                val pictureResponse = webApi.addNewPictureForKnownPerson(
                                    blindUsernamePart,
                                    personIdPart,
                                    picturePart
                                )
                                if (pictureResponse.isSuccessful) {
                                    Log.d(TAG_PERSONS, "KnownPerson add picture $index success")
                                } else {
                                    pictureErrors++
                                    // TODO handle error cases
                                    Log.e(TAG_PERSONS, "KnownPerson add picture $index fail")
                                    Log.e(
                                        TAG_PERSONS,
                                        "Fail: ${pictureResponse.code()} \"${pictureResponse.message()}\""
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                pictureErrors++
                            }
                        } else {
                            pictureErrors++
                            // TODO handle null case.
                            Log.e(TAG_PERSONS, "KnownPerson add picture $index fail")
                        }
                    }
                    if (pictureErrors == pictures.size) {
                        // If all the pictures failed to be uploaded.
                        appError = SimpleConnectionError("Error!", "Failed to upload the pictures.")
                        _state.postValue(State.ERROR)
                    } else {
                        // If at least 1 picture was uploaded successfully.
                        appError = NoError()
                        _registrationSuccessful.postValue(Pair(true, pictureErrors))
                        _state.postValue(State.NORMAL)
                    }
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