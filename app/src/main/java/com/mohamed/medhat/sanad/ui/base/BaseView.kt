package com.mohamed.medhat.sanad.ui.base

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import com.mohamed.medhat.sanad.model.error.AppError
import com.mohamed.medhat.sanad.ui.base.error_viewers.AppErrorViewer
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateAwareness

/**
 * The parent interface for all the mvp view interfaces.
 * It contains all the common functions among the mvp view interfaces.
 */
interface BaseView {
    /**
     * A function that is used to start a new activity from the current activity.
     * @param activity The class of the destination activity.
     * @param bundle The bundle object to pass to the next activity.
     */
    fun navigateTo(activity: Class<*>, bundle: Bundle? = null)

    /**
     * A function that is used to start a new activity from the current activity and finish the current activity.
     * @param activity The class of the destination activity.
     * @param bundle The bundle object to pass to the next activity.
     */
    fun navigateToThenFinish(activity: Class<*>, bundle: Bundle? = null)

    /**
     * Finishes the current activity with the passed `resultCode`.
     * @param resultCode The code to return to the calling activity.
     */
    fun finishWithResult(resultCode: Int)

    /**
     * This function starts a new activity as a root activity by using the two flags [Intent.FLAG_ACTIVITY_NEW_TASK]
     * and [Intent.FLAG_ACTIVITY_CLEAR_TASK].
     * @param activity The class of the destination activity.
     * @param bundle The bundle object to pass to the next activity.
     */
    fun startActivityAsRoot(activity: Class<*>, bundle: Bundle? = null)

    /**
     * A function that is used to display a toast message on the screen.
     * @param text the text value to be displayed.
     */
    fun displayToast(text: String)

    /**
     * Displays an error message in an input field.
     * @param editText the target [EditText] to display the error in.
     * @param error the error message to display.
     */
    fun showInputError(editText: EditText, error: String)

    /**
     * Resets the errors in a specific [EditText].
     */
    fun resetInputError(editText: EditText)

    /**
     * Defines the suitable behavior for the activity when the network state changes.
     * @param networkStateAwareness the behavior the activity will follow.
     */
    fun setNetworkStateAwareness(networkStateAwareness: NetworkStateAwareness)

    /**
     * Defines the suitable way to display errors in the activity.
     * @param appErrorViewer the way to display the error.
     */
    fun setAppErrorViewer(appErrorViewer: AppErrorViewer)

    /**
     * Displays an error in the activity. The error will belong to [AppError] family and it will be displayed in a way
     * specified by the function [setAppErrorViewer].
     */
    fun displayAppError()

    /**
     * Hides the [AppError] from the activity.
     */
    fun hideAppError()

    /**
     * Automatically checks for a permission, proceeds with an action if granted, and re-requests it if denied.
     * @param permission The permission string from [Manifest.permission] class.
     * @param title The permission dialog title if the permission was denied before.
     * @param message The permission dialog message if the permission was denied before.
     * @param positiveButtonLabel The label of the positive permission dialog if the permission was denied before.
     * @param negativeButtonLabel The label of the negative permission dialog if the permission was denied before.
     * @param permissionCode The unique permission code for the requested permission.
     * @param onGranted The action to proceed with if the permission was granted.
     */
    fun requestPermission(
        permission: String,
        title: String = "مطلوب إذن",
        message: String = "This permission is needed to use this feature",
        positiveButtonLabel: String = "حسنًا",
        negativeButtonLabel: String = "إلغاء",
        permissionCode: Int,
        onGranted: () -> Unit
    )

    /**
     * Opens the browser with the passed url.
     * @param url The website to open.
     */
    fun navigateToWebsite(url: String)

    /**
     * Opens the gallery to pick a picture from.
     * @param requestCode A request code to be caught in `onActivityResult`.
     * @param title An optional title for the image picker.
     */
    fun pickPictureFromGallery(requestCode: Int, title: String = "Select an image")

    /**
     * Launches the camera app to take a picture.
     * @param requestCode A request code to be caught in `onActivityResult`.
     * @param fileName The name of the file of the created picture.
     */
    fun takePhoto(requestCode: Int, fileName: String)

    /**
     * @return Bundle extras that was passed from the calling intent.
     */
    fun getExtras(): Bundle?
}