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
    fun navigateToActivity(activity: Class<*>)
}