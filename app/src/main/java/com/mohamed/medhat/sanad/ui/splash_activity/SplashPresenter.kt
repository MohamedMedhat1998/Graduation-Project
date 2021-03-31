package com.mohamed.medhat.sanad.ui.splash_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.ToastErrorViewer
import com.mohamed.medhat.sanad.utils.handleLoadingState
import javax.inject.Inject

/**
 * An mvp presenter for the [SplashActivity].
 */
@ActivityScope
class SplashPresenter @Inject constructor() :
    SimplePresenter<SplashView>() {

    private lateinit var splashView: SplashView
    private lateinit var splashActivity: SplashActivity
    private lateinit var splashNavViewModel: SplashNavViewModel

    override fun setView(view: SplashView) {
        splashView = view
        splashActivity = splashView as SplashActivity
    }

    fun setSplashNaveViewModel(splashNavViewModel: SplashNavViewModel) {
        this.splashNavViewModel = splashNavViewModel
    }

    override fun start(savedInstanceState: Bundle?) {
        splashNavViewModel.destination.observe(splashActivity) {
            if (it != null) {
                splashView.navigateToThenFinish(it)
            }
        }

        splashNavViewModel.state.observe(splashActivity) {
            splashView.setAppErrorViewer(
                ToastErrorViewer(
                    splashNavViewModel.appError,
                    splashActivity
                )
            )
            handleLoadingState(splashView, it)
        }
        NetworkState.isConnected.observe(splashActivity) {
            splashNavViewModel.calculateDestination()
        }
    }
}