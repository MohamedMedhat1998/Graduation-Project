package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.BlindPost
import com.mohamed.medhat.sanad.ui.add_blind_activity.blood_type.BloodType
import com.mohamed.medhat.sanad.ui.add_blind_activity.blood_type.BloodTypeAdapter
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessItem
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessesAdapter
import com.mohamed.medhat.sanad.ui.add_person_activity.pictures.PicturePreview
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.utils.EXTRA_SCANNED_SERIAL
import com.mohamed.medhat.sanad.utils.PERMISSION_ADD_BLIND_ACTIVITY_CAMERA
import com.mohamed.medhat.sanad.utils.generators.PictureNameGenerator
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
private const val TAG_ADD_BLIND_PICTURE_PREVIEW = "add-blind-picture-preview"

/**
 * An mvp presenter for [AddBlindActivity].
 */
class AddBlindPresenter @Inject constructor() :
    AdvancedPresenter<AddBlindView, AddBlindViewModel>() {

    private lateinit var addBlindView: AddBlindView
    private lateinit var addBlindViewModel: AddBlindViewModel
    private lateinit var addBlindActivity: AddBlindActivity
    private lateinit var rvIllnesses: RecyclerView
    private lateinit var illnessesAdapter: IllnessesAdapter
    private lateinit var rvBloodType: RecyclerView
    private lateinit var bloodTypeAdapter: BloodTypeAdapter
    private var imageUri: Uri? = null
    private var pictureName = ""

    override fun start(savedInstanceState: Bundle?) {
        initializeIllnessesRecyclerView()
        initializeBloodTypeRecyclerView()
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
                addBlindView.navigateToThenFinish(MainActivity::class.java)
                addBlindView.displayToast("Successfully registered!")
            }
        }
    }

    override fun setView(view: AddBlindView) {
        addBlindView = view
        addBlindActivity = view as AddBlindActivity
    }

    override fun setViewModel(viewModel: AddBlindViewModel) {
        addBlindViewModel = viewModel
    }

    /**
     * Sets up the initial configurations for the illnesses RecyclerView.
     */
    private fun initializeIllnessesRecyclerView() {
        rvIllnesses = addBlindActivity.rv_add_blind_illnesses
        rvIllnesses.layoutManager = LinearLayoutManager(addBlindActivity)
        illnessesAdapter = IllnessesAdapter(mutableListOf())
        rvIllnesses.adapter = illnessesAdapter
    }

    /**
     * Sets up the initial configurations for the blood type RecyclerView.
     */
    private fun initializeBloodTypeRecyclerView() {
        rvBloodType = addBlindActivity.rv_add_blind_blood_type
        rvBloodType.layoutManager = GridLayoutManager(addBlindActivity, 2)
        val bloodTypes = mutableListOf<BloodType>()
        addBlindActivity.resources.getStringArray(R.array.blood_types).forEachIndexed { index, it ->
            if (index == 0) {
                bloodTypes.add(BloodType(it, true))
            } else {
                bloodTypes.add(BloodType(it, false))
            }
        }
        bloodTypeAdapter = BloodTypeAdapter(bloodTypes)
        rvBloodType.adapter = bloodTypeAdapter
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
            pictureName = PictureNameGenerator.getNewPictureName()
            addBlindView.takePhoto(ADD_BLIND_TAKE_PHOTO, pictureName)
        }
    }

    fun handleOnRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ADD_BLIND_ACTIVITY_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pictureName = PictureNameGenerator.getNewPictureName()
                addBlindView.takePhoto(ADD_BLIND_TAKE_PHOTO, pictureName)
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
                        File("${addBlindActivity.getExternalFilesDir(Environment.DIRECTORY_DCIM)}/$pictureName.png")
                    Log.d("Picture", "Name: $pictureName Size: ${picture.length() / 1024.0}KB")
                    imageUri = Uri.fromFile(picture)
                    addBlindActivity.updateProfilePreviewImage(imageUri)
                } else {
                    addBlindView.displayToast("Something went wrong while taking a photo!")
                }
            }
        }
    }

    fun onAddIllnessClicked() {
        if (addBlindView.getOtherIllness().isNotEmpty()) {
            illnessesAdapter.addIllness(IllnessItem(addBlindView.getOtherIllness(), true))
            rvIllnesses.smoothScrollToPosition(illnessesAdapter.itemCount - 1)
            addBlindView.clearOtherIllness()
        } else {
            addBlindView.showInputError(
                addBlindActivity.et_add_blind_other_illness,
                addBlindActivity.getString(R.string.empty_field_warning)
            )
        }
    }

    fun onAddPictureClicked() {
        if (addBlindView.getAddPictureAnimationProgress() == 0.0f) {
            addBlindView.startAddPictureAnimation()
            return
        }
        if (addBlindView.getAddPictureAnimationProgress() == 1.0f) {
            addBlindView.reverseAddPictureAnimation()
            return
        }
    }

    fun onPreviewClicked() {
        if (imageUri != null) {
            PicturePreview(imageUri!!).show(
                addBlindActivity.supportFragmentManager,
                TAG_ADD_BLIND_PICTURE_PREVIEW
            )
        }
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
        if (addBlindActivity.et_add_blind_nickname.text.toString().isEmpty()) {
            addBlindView.showInputError(addBlindActivity.et_add_blind_nickname, emptyFieldError)
            addBlindActivity.add_blind_root.post {
                addBlindActivity.add_blind_root.smoothScrollTo(
                    0,
                    addBlindActivity.et_add_blind_nickname.top
                )
            }
            return
        }
        if (addBlindActivity.et_add_blind_phone.text.toString().isEmpty()) {
            addBlindView.showInputError(addBlindActivity.et_add_blind_phone, emptyFieldError)
            addBlindActivity.add_blind_root.post {
                addBlindActivity.add_blind_root.smoothScrollTo(
                    0,
                    addBlindActivity.et_add_blind_phone.top
                )
            }
            return
        }
        val name = addBlindView.getName()
        val lastName = addBlindView.getLastName()
        val age = addBlindView.getAge()
        val phone = addBlindView.getPhoneNumber()
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
            val lastNamePart =
                MultipartBody.Part.createFormData("LastName", lastName)
            // TODO update constant values.
            val emergencyPhoneNumberPart =
                MultipartBody.Part.createFormData("EmergencyPhoneNumber", "01234567899")
            val phoneNumberPart =
                MultipartBody.Part.createFormData("PhoneNumber", phone)
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
                    lastName = lastNamePart,
                    emergencyPhoneNumber = emergencyPhoneNumberPart,
                    phoneNumber = phoneNumberPart,
                    serialNumber = serialNumberPart
                )
            )
        } else {
            addBlindView.displayToast(addBlindActivity.getString(R.string.add_blind_pic_warning))
        }
    }
}