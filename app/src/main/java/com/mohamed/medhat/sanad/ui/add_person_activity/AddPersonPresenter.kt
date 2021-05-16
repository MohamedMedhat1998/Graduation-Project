package com.mohamed.medhat.sanad.ui.add_person_activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.KnownPersonData
import com.mohamed.medhat.sanad.ui.add_person_activity.pictures.PicturePreview
import com.mohamed.medhat.sanad.ui.add_person_activity.pictures.PicturesAdapter
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.utils.EXTRA_BLIND_PROFILE
import com.mohamed.medhat.sanad.utils.PERMISSION_ADD_BLIND_ACTIVITY_CAMERA
import com.mohamed.medhat.sanad.utils.PREFS_USER_EMAIL
import com.mohamed.medhat.sanad.utils.generators.PictureNameGenerator
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_add_person.*
import java.io.File
import javax.inject.Inject


private const val NEEDED_PICTURES_NUMBER = 3
private const val ADD_PERSON_PICK_FROM_GALLERY = 1
private const val ADD_PERSON_TAKE_PHOTO = 2
private const val PICTURE_PREVIEW_TAG = "picture-preview"

/**
 * An mvp presenter for the [AddPersonActivity].
 */
class AddPersonPresenter @Inject constructor(val sharedPrefs: SharedPrefs) :
    AdvancedPresenter<AddPersonView, AddPersonViewModel>() {

    lateinit var activity: AddPersonActivity
    lateinit var addPersonView: AddPersonView
    lateinit var addPersonViewModel: AddPersonViewModel
    private lateinit var blindMiniProfile: BlindMiniProfile
    private lateinit var picturesRecyclerView: RecyclerView
    private lateinit var picturesAdapter: PicturesAdapter
    private val pictures = mutableListOf<Uri>()
    private var pictureName = ""

    override fun start(savedInstanceState: Bundle?) {
        initializePicturesRecyclerView()
        addPersonViewModel.shouldReLogin.observe(activity) {
            if (it) {
                addPersonView.startActivityAsRoot(LoginActivity::class.java)
            }
        }
        addPersonViewModel.registrationSuccessful.observe(activity) {
            if (it.first) {
                if (it.second == 0) {
                    addPersonView.displayToast("Successfully registered a new person!")
                } else {
                    addPersonView.displayToast("Success, but failed to upload ${it.second} images.")
                }
                addPersonView.finishWithResult(Activity.RESULT_OK)
            }
        }
        addPersonViewModel.state.observe(activity) {
            val appError = addPersonViewModel.appError
            addPersonView.setAppErrorViewer(TextErrorViewer(appError, activity.tv_add_person_error))
            handleLoadingState(addPersonView, it)
        }
    }

    override fun setView(view: AddPersonView) {
        addPersonView = view
        activity = view as AddPersonActivity
        blindMiniProfile =
            activity.intent.extras?.getSerializable(EXTRA_BLIND_PROFILE) as BlindMiniProfile
    }

    override fun setViewModel(viewModel: AddPersonViewModel) {
        addPersonViewModel = viewModel
    }

    fun onPickFromGalleryClicked() {
        addPersonView.pickPictureFromGallery(ADD_PERSON_PICK_FROM_GALLERY)
    }

    fun onTakePhotoClicked() {
        addPersonView.requestPermission(
            permission = Manifest.permission.CAMERA,
            message = "The app wants to access the camera to take a picture.",
            permissionCode = PERMISSION_ADD_BLIND_ACTIVITY_CAMERA
        ) {
            pictureName = PictureNameGenerator.getNewPictureName()
            addPersonView.takePhoto(ADD_PERSON_TAKE_PHOTO, pictureName)
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
                addPersonView.takePhoto(ADD_PERSON_TAKE_PHOTO, pictureName)
            } else {
                addPersonView.displayToast("Unable to proceed without the permission.")
            }
        }
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("RequestCode", requestCode.toString())
        when (requestCode) {
            ADD_PERSON_PICK_FROM_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        pictures.add(imageUri)
                        picturesAdapter.updatePictures(pictures)
                    } else {
                        addPersonView.displayToast("Something went wrong while selecting an image!")
                    }
                } else {
                    addPersonView.displayToast("Something went wrong while selecting an image!")
                }
            }
            ADD_PERSON_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("Picture", "Result of from camera")
                    val picture =
                        File("${activity.getExternalFilesDir(Environment.DIRECTORY_DCIM)}/$pictureName.png")
                    Log.d("Picture", "Name: $pictureName Size: ${picture.length() / 1024.0}KB")
                    val imageUri = Uri.fromFile(picture)
                    if (imageUri != null) {
                        pictures.add(imageUri)
                        picturesAdapter.updatePictures(pictures)
                    } else {
                        addPersonView.displayToast("Something went wrong while taking a photo!")
                    }
                } else {
                    addPersonView.displayToast("Something went wrong while taking a photo!")
                }
            }
        }
        if (pictures.size < 3) {
            addPersonView.enablePictureButtons()
        } else {
            addPersonView.disablePictureButtons()
        }
    }

    fun onDoneClicked() {
        if (addPersonView.getName().isEmpty()) {
            addPersonView.showInputError(
                activity.et_add_person_name,
                activity.getString(R.string.empty_field_warning)
            )
            return
        }
        if (pictures.size < NEEDED_PICTURES_NUMBER) {
            addPersonView.displayToast("You must select $NEEDED_PICTURES_NUMBER pictures")
            return
        }
        val knownPersonData =
            KnownPersonData(
                name = addPersonView.getName(),
                reminderAbout = "",
                mentorUsername = sharedPrefs.read(
                    PREFS_USER_EMAIL
                ),
                blindUsername = blindMiniProfile.userName
            )
        addPersonViewModel.addNewPerson(knownPersonData, pictures, blindMiniProfile, activity)
    }

    /**
     * Sets up the "pictures recycler view".
     */
    private fun initializePicturesRecyclerView() {
        picturesRecyclerView = activity.rv_add_person_pictures
        picturesAdapter = PicturesAdapter(mutableListOf(), onDeletePictureClicked = {
            pictures.remove(it)
            if (pictures.size < 3) {
                addPersonView.enablePictureButtons()
            } else {
                addPersonView.disablePictureButtons()
            }
        }) {
            // TODO display the image in a dialog
            PicturePreview(it).show(activity.supportFragmentManager, PICTURE_PREVIEW_TAG)
        }
        picturesRecyclerView.layoutManager = GridLayoutManager(activity, 3)
        picturesRecyclerView.adapter = picturesAdapter
    }
}