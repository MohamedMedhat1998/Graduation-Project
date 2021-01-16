package com.mohamed.medhat.graduation_project.ui.base

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
}