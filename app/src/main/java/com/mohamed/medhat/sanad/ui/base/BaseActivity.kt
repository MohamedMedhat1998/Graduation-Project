package com.mohamed.medhat.sanad.ui.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mohamed.medhat.sanad.dagger.DaggerApplication
import com.mohamed.medhat.sanad.dagger.components.ActivityComponent
import com.mohamed.medhat.sanad.ui.base.error_viewers.AppErrorViewer
import com.mohamed.medhat.sanad.ui.base.error_viewers.NoErrorViewer
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateAware
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateAwareness

/**
 * A base class for all the activities in the app. This class is meant to reduce the boilerplate code for the activity classes.
 */
open class BaseActivity : AppCompatActivity(), BaseView {
    lateinit var activityComponent: ActivityComponent
    private var networkStateAwareness: NetworkStateAwareness = NetworkStateAware()
    private var appErrorViewer: AppErrorViewer = NoErrorViewer()

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent =
            (application as DaggerApplication).appComponent.activityComponent().create()
        super.onCreate(savedInstanceState)
        networkStateAwareness.handleNetworkStateChangeBehavior(this)
    }

    override fun navigateTo(activity: Class<*>, bundle: Bundle?) {
        val activityIntent = Intent(this, activity)
        if (bundle != null) {
            activityIntent.putExtras(bundle)
        }
        startActivity(activityIntent)
    }

    override fun navigateToThenFinish(activity: Class<*>, bundle: Bundle?) {
        navigateTo(activity, bundle)
        finish()
    }

    override fun finishWithResult(resultCode: Int) {
        setResult(resultCode)
        finish()
    }

    override fun startActivityAsRoot(activity: Class<*>, bundle: Bundle?) {
        val rootActivityIntent = Intent(this, activity)
        if (bundle != null) {
            rootActivityIntent.putExtras(bundle)
        }
        rootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        rootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(rootActivityIntent)
    }

    override fun displayToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun showInputError(editText: EditText, error: String) {
        editText.error = error
    }

    override fun resetInputError(editText: EditText) {
        editText.error = null
    }

    override fun setNetworkStateAwareness(networkStateAwareness: NetworkStateAwareness) {
        this.networkStateAwareness = networkStateAwareness
    }

    override fun setAppErrorViewer(appErrorViewer: AppErrorViewer) {
        this.appErrorViewer = appErrorViewer
    }

    override fun displayAppError() {
        appErrorViewer.display()
    }

    override fun hideAppError() {
        appErrorViewer.hide()
    }

    override fun requestPermission(
        permission: String,
        title: String,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String,
        permissionCode: Int,
        onGranted: () -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, proceed with the passed action.
            onGranted.invoke()
        } else {
            // Permission denied, re-request the permission.
            requestPermission(
                permission,
                title,
                message,
                positiveButtonLabel,
                negativeButtonLabel,
                permissionCode
            )
        }
    }

    override fun navigateToWebsite(url: String) {
        val websiteIntent = Intent(Intent.ACTION_VIEW)
        websiteIntent.data = Uri.parse(url)
        startActivity(websiteIntent)
    }

    override fun pickPictureFromGallery(requestCode: Int, title: String) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, title), requestCode)
    }

    override fun takePhoto(requestCode: Int, fileName: String) {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri =
            Uri.parse("file://${getExternalFilesDir(Environment.DIRECTORY_DCIM)}/$fileName.png")
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(takePicture, requestCode)
    }

    override fun getExtras(): Bundle? {
        return intent.extras
    }

    /**
     * Requests a permission.
     * @param permission The permission string from [Manifest.permission] class.
     * @param title The permission dialog title if the permission was denied before.
     * @param message The permission dialog message if the permission was denied before.
     * @param positiveButtonLabel The label of the positive permission dialog if the permission was denied before.
     * @param negativeButtonLabel The label of the negative permission dialog if the permission was denied before.
     * @param permissionCode The unique permission code for the requested permission.
     */
    private fun requestPermission(
        permission: String,
        title: String,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String,
        permissionCode: Int
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            // What to show to the user if he/she denied the permission before and then tries to access it again
            newPermissionDialog(
                permission,
                title,
                message,
                positiveButtonLabel,
                negativeButtonLabel,
                permissionCode
            )
        } else {
            // Re-request the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), permissionCode)
        }
    }

    /**
     * Creates and displays an [AlertDialog] specified for permissions.
     * @param permission The permission string from [Manifest.permission] class.
     * @param title Dialog title.
     * @param message Dialog message.
     * @param positiveButtonLabel The text of the positive button.
     * @param negativeButtonLabel The text of the negative button.
     * @param permissionCode The unique permission code for the requested permission.
     */
    private fun newPermissionDialog(
        permission: String,
        title: String,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String,
        permissionCode: Int
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                positiveButtonLabel
            ) { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    permissionCode
                )
            }
            .setNegativeButton(
                negativeButtonLabel
            ) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }
}