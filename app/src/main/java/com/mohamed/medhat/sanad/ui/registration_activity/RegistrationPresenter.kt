package com.mohamed.medhat.sanad.ui.registration_activity

import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import androidx.core.widget.addTextChangedListener
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.NewUser
import com.mohamed.medhat.sanad.ui.CommonNavViewModel
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.utils.handleLoadingState
import com.mohamed.medhat.sanad.utils.managers.TokenManager
import kotlinx.android.synthetic.main.activity_registration.*
import javax.inject.Inject

/**
 * An mvp presenter for the [RegistrationActivity].
 */
@ActivityScope
class RegistrationPresenter @Inject constructor(val tokenManager: TokenManager) :
    AdvancedPresenter<RegistrationView, RegistrationViewModel>() {

    private lateinit var registrationView: RegistrationView
    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var activity: RegistrationActivity
    private lateinit var commonNavViewModel: CommonNavViewModel

    override fun start(savedInstanceState: Bundle?) {
        fixPasswordGravity()
        registrationViewModel.token.observe(activity) {
            tokenManager.save(it)
            registrationView.displayToast(activity.getString(R.string.successfully_registered))
            commonNavViewModel.calculateDestination()
        }
        commonNavViewModel.destination.observe(activity) {
            if (it != null) {
                registrationView.startActivityAsRoot(it)
            }
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
        commonNavViewModel.state.observe(activity) {
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
        activity = (registrationView as RegistrationActivity)
    }

    override fun setViewModel(viewModel: RegistrationViewModel) {
        registrationViewModel = viewModel
    }

    fun setNavigationViewModel(commonNavViewModel: CommonNavViewModel) {
        this.commonNavViewModel = commonNavViewModel
    }

    fun onLoginClicked() {
        registrationView.navigateTo(LoginActivity::class.java)
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
                registrationView.showInputError(
                    activity.et_registration_password,
                    passwordMatchingError
                )
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

    private fun fixPasswordGravity() {
        activity.et_registration_password.addTextChangedListener {
            if (it.toString().isEmpty()) {
                activity.et_registration_password.gravity = Gravity.END or Gravity.CENTER
            } else {
                activity.et_registration_password.gravity = Gravity.START or Gravity.CENTER
            }
        }
        activity.et_registration_confirm_password.addTextChangedListener {
            if (it.toString().isEmpty()) {
                activity.et_registration_confirm_password.gravity = Gravity.END or Gravity.CENTER
            } else {
                activity.et_registration_confirm_password.gravity = Gravity.START or Gravity.CENTER
            }
        }
    }
}