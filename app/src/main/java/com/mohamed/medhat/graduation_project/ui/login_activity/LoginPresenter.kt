package com.mohamed.medhat.graduation_project.ui.login_activity

import android.os.Bundle
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import javax.inject.Inject

/**
 * An mvp presenter for [LoginActivity].
 */
@ActivityScope
class LoginPresenter @Inject constructor() : AdvancedPresenter<LoginView, LoginViewModel>() {

    private lateinit var loginView: LoginView
    private lateinit var loginViewModel: LoginViewModel

    override fun setView(view: LoginView) {
        loginView = view
    }

    override fun start(savedInstanceState: Bundle?) {
        (loginView as LoginActivity).displayToast("Started")
    }

    override fun setViewModel(viewModel: LoginViewModel) {
        loginViewModel = viewModel
    }

    fun onLoginClicked() {
        (loginView as LoginActivity).displayToast("Login \n${loginView.getEmail()} \n${loginView.getPassword()}")
    }

    fun onRegisterClicked() {
        (loginView as LoginActivity).displayToast("Register \n${loginView.getEmail()} \n${loginView.getPassword()}")
    }
}