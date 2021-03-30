package com.mohamed.medhat.sanad.ui.splash_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.managers.TokenManager
import javax.inject.Inject

/**
 * An mvp presenter for the [SplashActivity].
 */
@ActivityScope
class SplashPresenter @Inject constructor(val tokenManager: TokenManager) :
    SimplePresenter<SplashView>() {

    private lateinit var splashView: SplashView
    private lateinit var splashNavViewModel: SplashNavViewModel

    override fun setView(view: SplashView) {
        splashView = view
    }

    fun setSplashNaveViewModel(splashNavViewModel: SplashNavViewModel) {
        this.splashNavViewModel = splashNavViewModel
    }

    override fun start(savedInstanceState: Bundle?) {
        // TODO handle the normal/loading/error state
        splashNavViewModel.destination.observe(splashView as SplashActivity, {
            if (it != null) {
                splashView.navigateToThenFinish(it)
            } else {
                // TODO handle this error case
                splashView.displayToast("Something went wrong while selecting a destination")
            }
        })

        splashNavViewModel.state.observe(splashView as SplashActivity, {
            when (it) {
                State.ERROR -> splashView.displayToast("ERROR")
                State.LOADING -> splashView.displayToast("LOADING")
                State.NORMAL -> splashView.displayToast("NORMAL")
            }
        })

        splashNavViewModel.calculateDestination()
    }
}