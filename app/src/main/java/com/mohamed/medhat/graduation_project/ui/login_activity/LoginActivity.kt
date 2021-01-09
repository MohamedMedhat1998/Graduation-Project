package com.mohamed.medhat.graduation_project.ui.login_activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        activityComponent.inject(this)
        loginPresenter.setView(this)
        loginPresenter.setViewModel(loginViewModel)
        loginPresenter.start(savedInstanceState)
        btn_login_login.setOnClickListener {
            loginPresenter.onLoginClicked()
        }
        btn_login_register.setOnClickListener {
            loginPresenter.onRegisterClicked()
        }
    }

    override fun getEmail(): String {
        return et_login_email.text.toString()
    }

    override fun getPassword(): String {
        return et_login_password.text.toString()
    }
}