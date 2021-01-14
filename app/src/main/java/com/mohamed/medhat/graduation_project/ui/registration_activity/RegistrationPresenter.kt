package com.mohamed.medhat.graduation_project.ui.registration_activity

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.model.NewUser
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import kotlinx.android.synthetic.main.activity_registration.*
import javax.inject.Inject

/**
 * An mvp presenter for the [RegistrationActivity].
 */
@ActivityScope
class RegistrationPresenter @Inject constructor() :
    AdvancedPresenter<RegistrationView, RegistrationViewModel>() {

    private lateinit var registrationView: RegistrationView
    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var activity: RegistrationActivity

    // TODO handle the loading icon
    // TODO implement add picture
    // TODO test the configuration change
    override fun start(savedInstanceState: Bundle?) {
        activity = (registrationView as RegistrationActivity)
        activity.displayToast("RegistrationActivity")
        registrationViewModel.token.observe(activity) {
            // TODO save token in the shared prefs
            Log.d("TOKEN", "Success, your token is: ${it.token}")
        }
    }

    override fun setView(view: RegistrationView) {
        registrationView = view
    }

    override fun setViewModel(viewModel: RegistrationViewModel) {
        registrationViewModel = viewModel
    }

    fun onAddPictureClicked() {
        activity.displayToast("AddPicture")
    }

    fun onSubmitClicked() {
        resetErrors()
        if (validUserInputs()) {
            val newUser = NewUser(
                fullName = activity.getFullName(),
                email = activity.getEmail(),
                password = activity.getPassword(),
                confirmPassword = activity.getConfirmedPassword()
            )
            registrationViewModel.registerNewUser(newUser)
        }
    }

    /**
     * A helper function that checks the correctness of the input values from the user.
     * @return `true` if all the inputs are valid, `false` otherwise.
     */
    private fun validUserInputs(): Boolean {
        return !areEmpty() && passwordOk() && emailOk()
    }

    /**
     * @return `true` if any of the input fields is empty, `false` otherwise.
     */
    private fun areEmpty(): Boolean {
        // Getting the values from the input fields.
        val fullName = activity.getFullName()
        val email = activity.getEmail()
        val password = activity.getPassword()
        val confirmedPassword = activity.getConfirmedPassword()
        val emptyError = activity.getString(R.string.empty_field_warning)
        // Checking if these values are empty.
        if (fullName.isEmpty()) {
            activity.showInputError(activity.et_registration_full_name, emptyError)
            return true
        }
        if (email.isEmpty()) {
            activity.showInputError(activity.et_registration_email, emptyError)
            return true
        }
        if (password.isEmpty()) {
            activity.showInputError(activity.et_registration_password, emptyError)
            return true
        }
        if (confirmedPassword.isEmpty()) {
            activity.showInputError(activity.et_registration_confirm_password, emptyError)
            return true
        }
        return false
    }

    /**
     * @return `true` if the **password** doesn't contain problems, `false` otherwise.
     */
    private fun passwordOk(): Boolean {
        // Checking password matching.
        if (activity.getPassword() != activity.getConfirmedPassword()) {
            val passwordMatchingError = activity.getString(R.string.password_matching_warning)
            activity.displayToast(passwordMatchingError)
            activity.showInputError(activity.et_registration_password, passwordMatchingError)
            activity.showInputError(
                activity.et_registration_confirm_password,
                passwordMatchingError
            )
            return false
        }
        // TODO Check password constraints.
        return true
    }

    /**
     * @return `true` if the **email** doesn't contain problems, `false` otherwise.
     */
    private fun emailOk(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(activity.getEmail()).matches()) {
            activity.showInputError(
                activity.et_registration_email,
                activity.getString(R.string.invalid_email_warning)
            )
            return false
        }
        return true
    }

    /**
     * Resets the errors on the input fields.
     */
    private fun resetErrors() {
        activity.resetInputError(activity.et_registration_full_name)
        activity.resetInputError(activity.et_registration_email)
        activity.resetInputError(activity.et_registration_password)
        activity.resetInputError(activity.et_registration_confirm_password)
    }
}