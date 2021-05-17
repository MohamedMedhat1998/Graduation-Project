package com.mohamed.medhat.sanad.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.KnownPerson
import com.mohamed.medhat.sanad.model.KnownPersonData
import com.mohamed.medhat.sanad.model.error.AppError
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.*
import com.mohamed.medhat.sanad.utils.parsers.ApplicationConnectionErrorParser
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * A service that runs in the background to register a new [KnownPerson].
 */
class ServiceRegisterKnownPerson : JobIntentService() {

    @Inject
    lateinit var webApi: WebApi

    // convenient method for starting the service.
    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ServiceRegisterKnownPerson::class.java, 1, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        // Fetching data from the intent
        val extras = intent.extras
        if (extras == null) {
            Log.d("SERVICE", "extras is null")
            return
        }
        Log.d("SERVICE", "extras is not null")
        val knownPersonData =
            extras.getSerializable(EXTRA_KNOWN_PERSON_DATA) as KnownPersonData? ?: return
        val picturesList = extras.getStringArray(EXTRA_PICTURES_LIST) as Array<String>? ?: return
        val pictures = mutableListOf<Uri>()
        picturesList.forEach {
            pictures.add(Uri.parse(it))
        }
        val blindMiniProfile =
            extras.getSerializable(EXTRA_BLIND_PROFILE) as BlindMiniProfile? ?: return
        //--------------------------------
        //--------------------------------
        //--------------------------------
        var pictureErrors = 0
        var appError: AppError
        val localBroadcastManager =
            LocalBroadcastManager.getInstance(this@ServiceRegisterKnownPerson)
        // Registering the person's data (e.g. name, reminder about, ...etc)
        CoroutineScope(IO).launch {
            val response = webApi.addKnownPerson(knownPersonData)
            if (response.isSuccessful) {
                val knownPerson = response.body()
                if (knownPerson != null) {
                    // If registering the person's data was successful, start uploading his/her pictures.
                    pictures.forEachIndexed { index, it ->
                        val inputStream = baseContext.contentResolver.openInputStream(it)
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
                        localBroadcastManager.sendBroadcast(Intent(ACTION_ADD_KNOWN_PERSON).apply {
                            putExtra(EXTRA_STATE, State.ERROR)
                            putExtra(EXTRA_ERROR, appError)
                            putExtra(EXTRA_FAILED_PICTURES, pictureErrors)
                            putExtra(EXTRA_ADD_PERSON_SUCCESS, false)
                        })
                    } else {
                        // If at least 1 picture was uploaded successfully.
                        appError = NoError()
                        localBroadcastManager.sendBroadcast(Intent(ACTION_ADD_KNOWN_PERSON).apply {
                            putExtra(EXTRA_STATE, State.NORMAL)
                            putExtra(EXTRA_ERROR, appError)
                            putExtra(EXTRA_FAILED_PICTURES, pictureErrors)
                            putExtra(EXTRA_ADD_PERSON_SUCCESS, true)
                        })
                    }
                } else {
                    appError = SimpleConnectionError("Something went wrong!", "Please try again.")
                    localBroadcastManager.sendBroadcast(Intent(ACTION_ADD_KNOWN_PERSON).apply {
                        putExtra(EXTRA_STATE, State.ERROR)
                        putExtra(EXTRA_ERROR, appError)
                        putExtra(EXTRA_FAILED_PICTURES, pictureErrors)
                        putExtra(EXTRA_ADD_PERSON_SUCCESS, false)
                    })
                }
            } else {
                // If there was a problem while registering the user, display the suitable error.
                when (response.code()) {
                    408 -> { // Timed out, tell the user to retry.
                        appError = SimpleConnectionError("Request Timed Out!", "Please try again.")
                        localBroadcastManager.sendBroadcast(Intent(ACTION_ADD_KNOWN_PERSON).apply {
                            putExtra(EXTRA_STATE, State.ERROR)
                            putExtra(EXTRA_ERROR, appError)
                            putExtra(EXTRA_FAILED_PICTURES, pictureErrors)
                            putExtra(EXTRA_ADD_PERSON_SUCCESS, false)
                        })
                    }
                    401 -> { // Unauthorized, re-login.
                        localBroadcastManager.sendBroadcast(Intent(ACTION_ADD_KNOWN_PERSON).apply {
                            putExtra(EXTRA_STATE, State.ERROR)
                            putExtra(EXTRA_FAILED_PICTURES, pictureErrors)
                            putExtra(EXTRA_ADD_PERSON_SUCCESS, false)
                            putExtra(EXTRA_SHOULD_LOGIN, true)
                        })
                    }
                    in 500..511 -> { // Internal server error, tell the user to retry.
                        appError = SimpleConnectionError(
                            "Internal Server Error!",
                            "Please try again later."
                        )
                        localBroadcastManager.sendBroadcast(Intent(ACTION_ADD_KNOWN_PERSON).apply {
                            putExtra(EXTRA_STATE, State.ERROR)
                            putExtra(EXTRA_ERROR, appError)
                            putExtra(EXTRA_FAILED_PICTURES, pictureErrors)
                            putExtra(EXTRA_ADD_PERSON_SUCCESS, false)
                        })
                    }
                    400 -> { // Application Error, display the detailed error.
                        withContext(IO) {
                            appError = ApplicationConnectionErrorParser.parse(
                                response.errorBody()?.string().toString()
                            )
                            localBroadcastManager.sendBroadcast(Intent(ACTION_ADD_KNOWN_PERSON).apply {
                                putExtra(EXTRA_STATE, State.ERROR)
                                putExtra(EXTRA_ERROR, appError)
                                putExtra(EXTRA_FAILED_PICTURES, pictureErrors)
                                putExtra(EXTRA_ADD_PERSON_SUCCESS, false)
                            })
                        }
                    }
                    else -> { // Unknown error, tell the user about it.
                        appError =
                            SimpleConnectionError("Unknown Error!", "Please try again later.")
                        localBroadcastManager.sendBroadcast(Intent(ACTION_ADD_KNOWN_PERSON).apply {
                            putExtra(EXTRA_STATE, State.ERROR)
                            putExtra(EXTRA_ERROR, appError)
                            putExtra(EXTRA_FAILED_PICTURES, pictureErrors)
                            putExtra(EXTRA_ADD_PERSON_SUCCESS, false)
                        })
                    }
                }
            }
        }
    }
}