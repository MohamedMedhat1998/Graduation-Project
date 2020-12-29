package com.mohamed.medhat.graduation_project.ui.base

/**
 * The parent interface for all the mvp view interfaces.
 * It contains all the common functions among the mvp view interfaces.
 */
interface BaseView {
    fun navigateToActivity(activity: Class<*>)
}