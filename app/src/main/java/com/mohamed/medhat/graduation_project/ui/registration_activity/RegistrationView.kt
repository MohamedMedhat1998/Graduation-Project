package com.mohamed.medhat.graduation_project.ui.registration_activity

import com.mohamed.medhat.graduation_project.ui.base.BaseView

/**
 * An mvp view for the [RegistrationActivity].
 */
interface RegistrationView : BaseView {
    fun getFirstName(): String
    fun getLastName(): String
    fun getPhoneNumber(): String
    fun getEmail(): String
    fun getPassword(): String
}