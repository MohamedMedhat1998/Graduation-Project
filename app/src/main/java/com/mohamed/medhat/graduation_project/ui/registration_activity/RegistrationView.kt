package com.mohamed.medhat.graduation_project.ui.registration_activity

import com.mohamed.medhat.graduation_project.ui.base.BaseView
import com.mohamed.medhat.graduation_project.ui.base.LoadingFeatureOwner

/**
 * An mvp view for the [RegistrationActivity].
 */
interface RegistrationView : BaseView, LoadingFeatureOwner {
    fun getFirstName(): String
    fun getLastName(): String
    fun getEmail(): String
    fun getPassword(): String
    fun getConfirmedPassword(): String
}