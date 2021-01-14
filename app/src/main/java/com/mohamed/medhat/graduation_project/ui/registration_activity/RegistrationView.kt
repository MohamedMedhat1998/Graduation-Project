package com.mohamed.medhat.graduation_project.ui.registration_activity

import android.widget.EditText
import com.mohamed.medhat.graduation_project.ui.base.BaseView

/**
 * An mvp view for the [RegistrationActivity].
 */
interface RegistrationView : BaseView {
    fun getFullName(): String
    fun getEmail(): String
    fun getPassword(): String
    fun getConfirmedPassword(): String
    fun showInputError(editText: EditText, error: String)
    fun resetInputError(editText: EditText)
}