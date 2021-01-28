package com.mohamed.medhat.graduation_project.ui.base

import android.widget.EditText
import com.mohamed.medhat.graduation_project.ui.base.network_state_awareness.NetworkStateAwareness

/**
 * The parent interface for all the mvp view interfaces.
 * It contains all the common functions among the mvp view interfaces.
 */
interface BaseView {
    /**
     * A function that is used to start a new activity from the current activity.
     * @param activity the class of the destination activity.
     */
    fun navigateTo(activity: Class<*>)

    /**
     * A function that is used to start a new activity from the current activity and finish the current activity.
     * @param activity the class of the destination activity.
     */
    fun navigateToThenFinish(activity: Class<*>)

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
}