package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.model.BlindPost
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessItem
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessesAdapter
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.utils.EXTRA_SCANNED_SERIAL
import kotlinx.android.synthetic.main.activity_add_blind.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import javax.inject.Inject


private const val ADD_BLIND_PICK_PICTURE = 1

/**
 * An mvp presenter for [AddBlindActivity].
 */
class AddBlindPresenter @Inject constructor(val illnessesAdapter: IllnessesAdapter) :
    AdvancedPresenter<AddBlindView, AddBlindViewModel>() {

    private lateinit var addBlindView: AddBlindView
    private lateinit var addBlindViewModel: AddBlindViewModel
    private lateinit var addBlindActivity: AddBlindActivity
    private lateinit var rvIllnesses: RecyclerView
    private lateinit var filePath: String
    private lateinit var imageInputStream: InputStream

    override fun start(savedInstanceState: Bundle?) {
        rvIllnesses = addBlindActivity.rv_add_blind_illnesses
        rvIllnesses.layoutManager = LinearLayoutManager(addBlindActivity)
        rvIllnesses.adapter = illnessesAdapter
    }

    override fun setView(view: AddBlindView) {
        addBlindView = view
        addBlindActivity = view as AddBlindActivity
    }

    override fun setViewModel(viewModel: AddBlindViewModel) {
        addBlindViewModel = viewModel
    }

    fun onAddPictureClicked() {
        addBlindView.pickPicture(ADD_BLIND_PICK_PICTURE)
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_BLIND_PICK_PICTURE) {
            if (resultCode == RESULT_OK) {
                val imageUri = data?.data
                addBlindView.updateProfilePreviewImage(imageUri)
                if (imageUri != null) {
                    val inputStream = addBlindActivity.contentResolver.openInputStream(imageUri)
                    if (inputStream != null) {
                        imageInputStream = inputStream
                    }
                }
                /*if (imageUri != null) {
                    filePath = getRealPathFromURI(imageUri)
                }*/
            } else {
                addBlindView.displayToast("Something went wrong while selecting an image!")
            }
        }
    }

    fun onAddIllnessClicked() {
        // TODO handle empty illness.
        illnessesAdapter.addIllness(IllnessItem(addBlindView.getOtherIllness(), true))
        rvIllnesses.smoothScrollToPosition(illnessesAdapter.itemCount - 1)
        addBlindView.clearOtherIllness()
    }

    fun onNextClicked() {
        val name = addBlindView.getName()
        val age = addBlindView.getAge()
        val gender = addBlindView.getGender()
        val bloodType = addBlindView.getBloodType()
        val illnesses = addBlindView.getIllnesses()
        val serialNumber = addBlindView.getExtras()?.get(EXTRA_SCANNED_SERIAL).toString()
        if (::imageInputStream.isInitialized) {
            val requestBody =
                imageInputStream.readBytes().toRequestBody("image/png".toMediaType())
            val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData(
                "File", "profile_picture_$name.png", requestBody
            )
            addBlindViewModel.addBlind(
                BlindPost(
                    firstName = name,
                    age = age,
                    gender = gender,
                    bloodType = bloodType,
                    illnesses = illnesses,
                    profilePicture = multipartBody,
                    lastName = "a", // TODO fix this
                    emergencyPhoneNumber = "012345678911",
                    phoneNumber = "012345678910",
                    serialNumber = serialNumber
                )
            )
        } else {
            // TODO fix error message
            addBlindView.displayToast("Something went wrong while picking an image")
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String {
        val result: String
        val cursor: Cursor? =
            addBlindActivity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path!!
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            Log.d("idx", idx.toString())
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}