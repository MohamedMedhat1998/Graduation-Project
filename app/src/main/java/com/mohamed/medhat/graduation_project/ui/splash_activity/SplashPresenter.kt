package com.mohamed.medhat.graduation_project.ui.splash_activity

import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import javax.inject.Inject

/**
 * An mvp presenter for the [SplashActivity].
 */
@ActivityScope
class SplashPresenter @Inject constructor() :
    AdvancedPresenter<SplashView, SplashViewModel>() {

    private lateinit var splashView: SplashView
    private lateinit var splashViewModel: SplashViewModel

    override fun setView(view: SplashView) {
        splashView = view
    }

    override fun setViewModel(viewModel: SplashViewModel) {
        splashViewModel = viewModel
    }
}