package com.mohamed.medhat.graduation_project.ui.splash_activity

import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.BasePresenter
import javax.inject.Inject

/**
 * An mvp presenter for the [SplashActivity].
 */
@ActivityScope
class SplashPresenter @Inject constructor() : BasePresenter<SplashView>() {

    private lateinit var splashView: SplashView

    override fun setView(view: SplashView) {
        splashView = view
    }
    //private val splashViewModel: SplashViewModel
}