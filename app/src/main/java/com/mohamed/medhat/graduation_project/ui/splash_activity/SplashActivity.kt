package com.mohamed.medhat.graduation_project.ui.splash_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
import javax.inject.Inject

/**
 * An activity for the splash screen.
 */
class SplashActivity : BaseActivity<SplashView>(), SplashView {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        appComponent.inject(this)
        initPresenter(splashPresenter)
    }

    override fun navigateToActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }
}