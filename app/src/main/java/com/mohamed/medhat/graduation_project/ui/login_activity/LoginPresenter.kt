package com.mohamed.medhat.graduation_project.ui.login_activity

import android.os.Bundle
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import com.mohamed.medhat.graduation_project.ui.registration_activity.RegistrationActivity
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
    }

    override fun setViewModel(viewModel: LoginViewModel) {
        loginViewModel = viewModel
    }

    fun onLoginClicked() {
        activity.displayToast("Login \n${loginView.getEmail()} \n${loginView.getPassword()}")
    }

    fun onRegisterClicked() {
        activity.displayToast("Register \n${loginView.getEmail()} \n${loginView.getPassword()}")
        activity.navigateToActivity(RegistrationActivity::class.java)
    }
}