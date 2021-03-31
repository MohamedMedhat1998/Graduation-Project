package com.mohamed.medhat.sanad.ui.login_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.LoginUser
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.registration_activity.RegistrationActivity
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * An mvp presenter for [LoginActivity].
 */
@ActivityScope
class LoginPresenter @Inject constructor() : AdvancedPresenter<LoginView, LoginViewModel>() {

    private lateinit var loginView: LoginView
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginNavViewModel: LoginNavViewModel
    private lateinit var activity: LoginActivity

    override fun setView(view: LoginView) {
        loginView = view
    }

    fun setNavigationViewModel(loginNavViewModel: LoginNavViewModel) {
        this.loginNavViewModel = loginNavViewModel
    }

    override fun start(savedInstanceState: Bundle?) {
        activity = (loginView as LoginActivity)
        loginViewModel.token.observe(activity) {
            loginView.apply {
                displayToast(activity.getString(R.string.successfully_logged_in))
                loginNavViewModel.calculateDestination()
            }
        }
        loginViewModel.state.observe(activity) {
            val textErrorViewer = TextErrorViewer(loginViewModel.appError, activity.tv_login_error)
            loginView.setAppErrorViewer(textErrorViewer)
            handleLoadingState(loginView, it)
        }
        loginNavViewModel.destination.observe(activity) {
            loginView.navigateToThenFinish(it)
        }
    }

    override fun setViewModel(viewModel: LoginViewModel) {
        loginViewModel = viewModel
    }

    fun onLoginClicked() {
        loginView.apply {
            hideError()
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