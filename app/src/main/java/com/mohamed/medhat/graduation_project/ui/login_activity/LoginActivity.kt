package com.mohamed.medhat.graduation_project.ui.login_activity

import android.content.Intent
import android.os.Bundle
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
import javax.inject.Inject

/**
 * An activity for the login screen.
 */
class LoginActivity : BaseActivity<LoginView>(), LoginView {

    @Inject
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        activityComponent.inject(this)
        initPresenter(loginPresenter)
    }

    override fun navigateToActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }
}