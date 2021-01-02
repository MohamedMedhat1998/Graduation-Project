package com.mohamed.medhat.graduation_project.ui.splash_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
import javax.inject.Inject

/**
 * An activity for the splash screen.
 */
class SplashActivity : BaseActivity(), SplashView {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    private val viewModel by lazy {
        ViewModelProviders
            .of(this, activityComponent.splashViewModel())
            .get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        activityComponent.inject(this)
        splashPresenter.setView(this)
        splashPresenter.setViewModel(viewModel)
    }

    override fun navigateToActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }
}