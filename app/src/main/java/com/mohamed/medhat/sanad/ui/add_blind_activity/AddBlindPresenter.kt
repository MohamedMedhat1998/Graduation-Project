package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.BlindPost
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessItem
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessesAdapter
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.utils.EXTRA_SCANNED_SERIAL
import com.mohamed.medhat.sanad.utils.PERMISSION_ADD_BLIND_ACTIVITY_CAMERA
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_add_blind.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream
import javax.inject.Inject


private const val ADD_BLIND_PICK_FROM_GALLERY = 1
private const val ADD_BLIND_TAKE_PHOTO = 2

/**
 * An mvp presenter for [AddBlindActivity].
 */
class AddBlindPresenter @Inject constructor() :
    AdvancedPresenter<AddBlindView, AddBlindViewModel>() {

    private lateinit var addBlindView: AddBlindView
    private lateinit var addBlindViewModel: AddBlindViewModel
    private lateinit var addBlindNavViewModel: AddBlindNavViewModel
    private lateinit var addBlindActivity: AddBlindActivity
    private lateinit var rvIllnesses: RecyclerView
    private lateinit var illnessesAdapter: IllnessesAdapter
    private var imageUri: Uri? = null

    override fun start(savedInstanceState: Bundle?) {
        rvIllnesses = addBlindActivity.rv_add_blind_illnesses
        rvIllnesses.layoutManager = LinearLayoutManager(addBlindActivity)
        val illnesses = mutableListOf<IllnessItem>()
        addBlindActivity.resources.getStringArray(R.array.illnesses).forEach {
            illnesses.add(IllnessItem(it, false))
        }
        illnessesAdapter = IllnessesAdapter(illnesses)
        rvIllnesses.adapter = illnessesAdapter
        addBlindViewModel.state.observe(addBlindActivity) {
            addBlindView.setAppErrorViewer(
                TextErrorViewer(
                    addBlindViewModel.appError,
                    addBlindActivity.tv_add_blind_error
                )
            )
            handleLoadingState(addBlindView, it)
        }
        addBlindViewModel.shouldReLogin.observe(addBlindActivity) {
            if (it) {
                addBlindView.navigateToThenFinish(LoginActivity::class.java)
            }
        }
        addBlindViewModel.isSuccessfulRegistration.observe(addBlindActivity) {
            if (it) {
                addBlindView.displayToast("Successfully registered!")
                addBlindNavViewModel.calculateDestination()
            }
        }
        addBlindNavViewModel.destination.observe(addBlindActivity) {
            addBlindView.navigateToThenFinish(it)
        }
    }

    override fun setView(view: AddBlindView) {
        addBlindView = view
        addBlindActivity = view as AddBlindActivity
    }

    override fun setViewModel(viewModel: AddBlindViewModel) {
        addBlindViewModel = viewModel
    }

    fun setNavigationViewModel(navViewModel: AddBlindNavViewModel) {
        addBlindNavViewModel = navViewModel
    }

    fun onPickFromGalleryClicked() {
        addBlindView.pickPictureFromGallery(ADD_BLIND_PICK_FROM_GALLERY)
    }

    fun onTakePhotoClicked() {
        addBlindView.requestPermission(
            permission = Manifest.permission.CAMERA,
            message = "The app wants to access the camera to take a picture.",
            permissionCode = PERMISSION_ADD_BLIND_ACTIVITY_CAMERA
        ) {
            addBlindView.takePhoto(ADD_BLIND_TAKE_PHOTO)
        }
    }

    fun handleOnRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ADD_BLIND_ACTIVITY_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addBlindView.takePhoto(ADD_BLIND_TAKE_PHOTO)
            } else {
                addBlindView.displayToast("Unable to proceed without the permission.")
            }
        }
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("RequestCode", requestCode.toString())
        when (requestCode) {
            ADD_BLIND_PICK_FROM_GALLERY -> {
                if (resultCode == RESULT_OK) {
                    imageUri = data?.data
                    addBlindView.updateProfilePreviewImage(imageUri)
                } else {
                    addBlindView.displayToast("Something went wrong while selecting an image!")
                }
            }
            ADD_BLIND_TAKE_PHOTO -> {
                if (resultCode == RESULT_OK) {
                    Log.d("Picture", "Result of from camera")
                    val picture =
                        File("${addBlindActivity.getExternalFilesDir(Environment.DIRECTORY_DCIM)}/photo.png")
                    Log.d("Picture", "Size: ${picture.length()/1024.0}KB")
                    imageUri = Uri.fromFile(picture)
                    addBlindActivity.updateProfilePreviewImage(imageUri)
                } else {
                    addBlindView.displayToast("Something went wrong while taking a photo!")
                }
            }
        }
    }

    fun onAddIllnessClicked() {
        illnessesAdapter.addIllness(IllnessItem(addBlindView.getOtherIllness(), true))
        rvIllnesses.smoothScrollToPosition(illnessesAdapter.itemCount - 1)
        addBlindView.clearOtherIllness()
    }

    fun onNextClicked() {
        val emptyFieldError = addBlindActivity.getString(R.string.empty_field_warning)
        if (addBlindView.getName().isEmpty()) {
            addBlindView.showInputError(addBlindActivity.et_add_blind_name, emptyFieldError)
            addBlindActivity.add_blind_root.post {
                addBlindActivity.add_blind_root.smoothScrollTo(
                    0,
                    addBlindActivity.et_add_blind_name.top
                )
            }
            return
        }
        if (addBlindActivity.et_add_blind_age.text.toString().isEmpty()) {
            addBlindView.showInputError(addBlindActivity.et_add_blind_age, emptyFieldError)
            addBlindActivity.add_blind_root.post {
                addBlindActivity.add_blind_root.smoothScrollTo(
                    0,
                    addBlindActivity.et_add_blind_age.top
                )
            }
            return
        }
        val name = addBlindView.getName()
        val age = addBlindView.getAge()
        val gender = addBlindView.getGender()
        val bloodType = addBlindView.getBloodType()
        val illnesses = addBlindView.getIllnesses()
        val serialNumber = addBlindView.getExtras()?.get(EXTRA_SCANNED_SERIAL).toString()
        var imageInputStream: InputStream? = null
        if (imageUri != null) {
            imageInputStream = addBlindActivity.contentResolver.openInputStream(imageUri!!)
        }
        if (imageInputStream != null) {
            val namePart =
                MultipartBody.Part.createFormData("FirstName", name)
            val agePart =
                MultipartBody.Part.createFormData("Age", age.toString())
            val genderPart =
                MultipartBody.Part.createFormData("Gender", gender.toString())
            val bloodTypePart =
                MultipartBody.Part.createFormData("BloodType", bloodType)
            val profileRequestBody =
                imageInputStream.readBytes().toRequestBody("application/octet-stream".toMediaType())
            val profilePicturePart = MultipartBody.Part.createFormData(
                "File",
                "profile_ss_picture_$name.png",
                profileRequestBody
            )
            // TODO update constant values after the UI is done
            val lastNamePart =
                MultipartBody.Part.createFormData("LastName", "last-name")
            // TODO update constant values after the UI is done
            val emergencyPhoneNumberPart =
                MultipartBody.Part.createFormData("EmergencyPhoneNumber", "01234567899")
            // TODO update constant values after the UI is done
            val phoneNumberPart =
                MultipartBody.Part.createFormData("PhoneNumber", "01234567809")
            val serialNumberPart =
                MultipartBody.Part.createFormData("SerialNumber", serialNumber)
            addBlindViewModel.addBlind(
                BlindPost(
                    firstName = namePart,
                    age = agePart,
                    gender = genderPart,
                    bloodType = bloodTypePart,
                    illnesses = illnesses,
                    profilePicture = profilePicturePart,
                    lastName = lastNamePart, // TODO fix this
                    emergencyPhoneNumber = emergencyPhoneNumberPart,
                    phoneNumber = phoneNumberPart,
                    serialNumber = serialNumberPart
                )
            )
        } else {
            // TODO fix error message
            addBlindView.displayToast("Please add a picture")
        }
    }
}