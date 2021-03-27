package com.mohamed.medhat.sanad.ui.registration_activity

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.NewUser
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.utils.handleLoadingState
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

    // TODO implement add picture
    override fun start(savedInstanceState: Bundle?) {
        activity = (registrationView as RegistrationActivity)
        registrationView.displayToast("RegistrationActivity")
        registrationViewModel.token.observe(activity) {
            // TODO save token in the shared prefs
            registrationView.apply {
                navigateToThenFinish(LoginActivity::class.java)
                displayToast(activity.getString(R.string.successfully_registered))
            }
            Log.d("TOKEN", "Success, your token is: ${it.token}")
        }
        registrationViewModel.state.observe(activity) {
            registrationView.setAppErrorViewer(
                TextErrorViewer(
                    registrationViewModel.appError,
                    activity.tv_registration_error
                )
            )
            handleLoadingState(registrationView, it)
        }
    }

    override fun setView(view: RegistrationView) {
        registrationView = view
    }

    override fun setViewModel(viewModel: RegistrationViewModel) {
        registrationViewModel = viewModel
    }

    fun onAddPictureClicked() {
        registrationView.displayToast("AddPicture")
    }

    fun onSubmitClicked() {
        resetErrors()
        if (validUserInputs()) {
            val newUser = NewUser(
                firstName = registrationView.getFirstName(),
                lastName = registrationView.getLastName(),
                email = registrationView.getEmail(),
                password = registrationView.getPassword(),
                confirmPassword = registrationView.getConfirmedPassword()
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
        val firstName = registrationView.getFirstName()
        val lastName = registrationView.getLastName()
        val email = registrationView.getEmail()
        val password = registrationView.getPassword()
        val confirmedPassword = registrationView.getConfirmedPassword()
        val emptyError = activity.getString(R.string.empty_field_warning)
        var errorFlag = false
        // Checking if these values are empty.
        if (firstName.isEmpty()) {
            registrationView.showInputError(activity.et_registration_first_name, emptyError)
            errorFlag = true
        }
        if (lastName.isEmpty()) {
            registrationView.showInputError(activity.et_registration_last_name, emptyError)
            errorFlag = true
        }
        if (email.isEmpty()) {
            registrationView.showInputError(activity.et_registration_email, emptyError)
            errorFlag = true
        }
        if (password.isEmpty()) {
            registrationView.showInputError(activity.et_registration_password, emptyError)
            errorFlag = true
        }
        if (confirmedPassword.isEmpty()) {
            registrationView.showInputError(activity.et_registration_confirm_password, emptyError)
            errorFlag = true
        }
        return errorFlag
    }

    /**
     * @return `true` if the **password** doesn't contain problems, `false` otherwise.
     */
    private fun passwordOk(): Boolean {
        var okFlag = true
        val password = registrationView.getPassword()
        val confirmPassword = registrationView.getConfirmedPassword()
        // Checking password matching.
        if (password != confirmPassword) {
            val passwordMatchingError = activity.getString(R.string.password_matching_warning)
            registrationView.apply {
                displayToast(passwordMatchingError)
                showInputError(
                    activity.et_registration_password,
                    passwordMatchingError
                )
                showInputError(
                    activity.et_registration_confirm_password,
                    passwordMatchingError
                )
            }
            okFlag = false
        } else {
            val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#\$^+=!*()@%&]).{8,}\$"
            if (!password.matches(passwordRegex.toRegex())) {
                okFlag = false
                // TODO move to strings
                val passwordMatchingError = "Min Length 8 Characters\n" +
                        "Should Include digit numbers\n" +
                        "Should Include Uppercase\n" +
                        "Should Include Lowercase\n" +
                        "Should Include Special Characters"
                registrationView.showInputError(activity.et_registration_password,passwordMatchingError)
            }
        }
        return okFlag
    }

    /**
     * @return `true` if the **email** doesn't contain problems, `false` otherwise.
     */
    private fun emailOk(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(registrationView.getEmail()).matches()) {
            registrationView.showInputError(
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
        registrationView.apply {
            hideError()
            hideLoadingIndicator()
            resetInputError(activity.et_registration_first_name)
            resetInputError(activity.et_registration_last_name)
            resetInputError(activity.et_registration_email)
            resetInputError(activity.et_registration_password)
            resetInputError(activity.et_registration_confirm_password)
        }
    }
}