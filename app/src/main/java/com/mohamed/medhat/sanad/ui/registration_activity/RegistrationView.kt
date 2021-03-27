package com.mohamed.medhat.sanad.ui.registration_activity

import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

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