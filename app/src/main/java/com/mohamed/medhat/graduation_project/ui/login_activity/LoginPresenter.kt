package com.mohamed.medhat.graduation_project.ui.login_activity

import android.os.Bundle
import android.util.Log
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.model.LoginUser
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import com.mohamed.medhat.graduation_project.ui.main_activity.MainActivity
import com.mohamed.medhat.graduation_project.ui.registration_activity.RegistrationActivity
import com.mohamed.medhat.graduation_project.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * An mvp presenter for [LoginActivity].
 */
@ActivityScope
class LoginPresenter @Inject constructor() : AdvancedPresenter<LoginView, LoginViewModel>() {

    private lateinit var loginView: LoginView
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var activity: LoginActivity

    override fun setView(view: LoginView) {
        loginView = view
    }

    override fun start(savedInstanceState: Bundle?) {
        activity = (loginView as LoginActivity)
        loginViewModel.token.observe(activity) {
            loginView.apply {
                displayToast(activity.getString(R.string.successfully_logged_in))
                navigateToThenFinish(MainActivity::class.java)
            }
        }
        loginViewModel.state.observe(activity) {
            handleLoadingState(loginView, loginViewModel.error, it)
        }
    }

    override fun setViewModel(viewModel: LoginViewModel) {
        loginViewModel = viewModel
    }

    fun onLoginClicked() {
        loginView.apply {
            hideErrorMessage()
            hideLoadingIndicator()
            resetInputError(activity.et_login_email)
            resetInputError(activity.et_login_password)
        }
        val emptyError = activity.getString(R.string.empty_field_warning)
        var somethingWrong = false
        if (loginView.getEmail().isEmpty()) {
            loginView.showInputError(
                activity.et_login_email,
                emptyError
            )
            somethingWrong = true
        }
        if (loginView.getPassword().isEmpty()) {
            loginView.showInputError(
                activity.et_login_password,
                emptyError
            )
            somethingWrong = true
        }
        if (!somethingWrong) {
            val loginUser = LoginUser(
                email = loginView.getEmail(),
                password = loginView.getPassword()
            )
            loginViewModel.login(loginUser)
        }
    }

    fun onRegisterClicked() {
        loginView.navigateTo(RegistrationActivity::class.java)
    }
}