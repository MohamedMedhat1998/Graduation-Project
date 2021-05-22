package com.mohamed.medhat.sanad.ui.mentor_picture_activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.utils.generators.PictureNameGenerator
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_mentor_picture.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

private const val MENTOR_PICTURE_PICK_FROM_GALLERY = 1
private const val MENTOR_PICTURE_TAKE_PHOTO = 2
private const val PERMISSION_MENTOR_PICTURE_ACTIVITY_CAMERA = 3

/**
 * An mvp presenter for [MentorPictureActivity].
 */
class MentorPicturePresenter @Inject constructor() :
    AdvancedPresenter<MentorPictureView, MentorPictureViewModel>() {

    lateinit var mentorPictureViewModel: MentorPictureViewModel
    lateinit var mentorPictureNavViewModel: MentorPictureNavViewModel
    lateinit var mentorPictureView: MentorPictureView
    lateinit var activity: MentorPictureActivity
    private var imageUri: Uri? = null
    private var pictureName = ""

    override fun start(savedInstanceState: Bundle?) {
        mentorPictureViewModel.state.observe(activity) {
            val errorViewer =
                TextErrorViewer(mentorPictureViewModel.appError, activity.tv_mentor_picture_error)
            mentorPictureView.setAppErrorViewer(errorViewer)
            handleLoadingState(mentorPictureView, it)
        }
        mentorPictureViewModel.shouldReLogin.observe(activity) {
            if (it) {
                mentorPictureView.startActivityAsRoot(LoginActivity::class.java)
            }
        }
        mentorPictureViewModel.uploadSuccess.observe(activity) {
            if (it) {
                mentorPictureNavViewModel.calculateDestination()
            }
        }
        mentorPictureNavViewModel.destination.observe(activity) {
            mentorPictureView.navigateToThenFinish(it)
        }
    }

    override fun setView(view: MentorPictureView) {
        mentorPictureView = view
        activity = view as MentorPictureActivity
    }

    override fun setViewModel(viewModel: MentorPictureViewModel) {
        mentorPictureViewModel = viewModel
    }

    fun setNavViewModel(navViewModel: MentorPictureNavViewModel) {
        mentorPictureNavViewModel = navViewModel
    }

    fun onPickFromGalleryClicked() {
        mentorPictureView.pickPictureFromGallery(MENTOR_PICTURE_PICK_FROM_GALLERY)
    }

    fun onTakePhotoClicked() {
        mentorPictureView.requestPermission(
            permission = Manifest.permission.CAMERA,
            message = "The app wants to access the camera to take a picture.",
            permissionCode = PERMISSION_MENTOR_PICTURE_ACTIVITY_CAMERA
        ) {
            pictureName = PictureNameGenerator.getNewPictureName()
            mentorPictureView.takePhoto(MENTOR_PICTURE_TAKE_PHOTO, pictureName)
        }
    }

    fun handleOnRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_MENTOR_PICTURE_ACTIVITY_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pictureName = PictureNameGenerator.getNewPictureName()
                mentorPictureView.takePhoto(MENTOR_PICTURE_TAKE_PHOTO, pictureName)
            } else {
                mentorPictureView.displayToast("Unable to proceed without the permission.")
            }
        }
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("RequestCode", requestCode.toString())
        when (requestCode) {
            MENTOR_PICTURE_PICK_FROM_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageUri = data?.data
                    mentorPictureView.updateProfilePicturePreview(imageUri)
                } else {
                    mentorPictureView.displayToast("Something went wrong while selecting an image!")
                }
            }
            MENTOR_PICTURE_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("Picture", "Result of from camera")
                    val picture =
                        File("${activity.getExternalFilesDir(Environment.DIRECTORY_DCIM)}/$pictureName.png")
                    Log.d("Picture", "Name: $pictureName Size: ${picture.length() / 1024.0}KB")
                    imageUri = Uri.fromFile(picture)
                    activity.updateProfilePicturePreview(imageUri)
                } else {
                    mentorPictureView.displayToast("Something went wrong while taking a photo!")
                }
            }
        }
    }

    fun onNextClicked() {
        if (imageUri == null) {
            mentorPictureView.displayToast("Please select an image")
            return
        }
        // TODO create a multipart request!
        val imageInputStream = activity.contentResolver.openInputStream(imageUri!!)
        val profilePictureBody =
            imageInputStream!!.readBytes().toRequestBody("application/octet-stream".toMediaType())
        val profilePicturePart =
            MultipartBody.Part.createFormData("File", pictureName, profilePictureBody)
        mentorPictureViewModel.uploadProfilePicture(profilePicturePart)
    }
}