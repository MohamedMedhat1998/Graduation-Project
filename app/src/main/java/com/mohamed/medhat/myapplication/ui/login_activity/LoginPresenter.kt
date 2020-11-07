package com.mohamed.medhat.myapplication.ui.login_activity

import com.mohamed.medhat.myapplication.ui.BasePresenter

class LoginPresenter(private val view: LoginView) : BasePresenter() {
    lateinit var loginViewModel: LoginViewModel
}