package com.mohamed.medhat.sanad.ui.login_activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.CommonNavViewModel
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * An activity for the login screen.
 */
class LoginActivity : BaseActivity(), LoginView {

    @Inject
    lateinit var loginPresenter: LoginPresenter

    private val loginViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.loginViewModel())
            .get(LoginViewModel::class.java)
    }

    private val commonNavViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.commonNavViewModel())
            .get(CommonNavViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        activityComponent.inject(this)
        loginPresenter.setView(this)
        loginPresenter.setViewModel(loginViewModel)
        loginPresenter.setNavigationViewModel(commonNavViewModel)
        loginPresenter.start(savedInstanceState)
        btn_login_login.setOnClickListener {
            loginPresenter.onLoginClicked()
        }
        btn_login_register.setOnClickListener {
            loginPresenter.onRegisterClicked()
        }
        btn_login_facebook.setOnClickListener {
            loginPresenter.onFacebookClicked()
        }
        btn_login_google.setOnClickListener {
            loginPresenter.onGoogleClicked()
        }
        tv_login_forget_password.setOnClickListener {
            loginPresenter.onForgetPasswordClicked()
        }
    }

    override fun getEmail(): String {
        return et_login_email.text.toString()
    }

    override fun getPassword(): String {
        return et_login_password.text.toString()
    }

    override fun showLoadingIndicator() {
        pb_login_loading.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_login_loading.visibility = View.INVISIBLE
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }
}