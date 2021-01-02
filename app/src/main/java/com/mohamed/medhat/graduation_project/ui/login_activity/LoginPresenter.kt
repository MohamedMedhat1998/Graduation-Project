package com.mohamed.medhat.graduation_project.ui.login_activity

import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.SimplePresenter
import javax.inject.Inject

/**
 * An mvp presenter for [LoginActivity].
 */
@ActivityScope
class LoginPresenter @Inject constructor() : SimplePresenter<LoginView>() {

    private lateinit var loginView: LoginView

    override fun setView(view: LoginView) {
        loginView = view
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}