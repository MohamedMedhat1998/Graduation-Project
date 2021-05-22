package com.mohamed.medhat.sanad.ui.splash_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.ui.CommonNavViewModel
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.ToastErrorViewer
import com.mohamed.medhat.sanad.utils.Checklist
import com.mohamed.medhat.sanad.utils.handleLoadingState
import javax.inject.Inject

private const val CHECKLIST_DESTINATION = "checklist-destination"
private const val CHECKLIST_ANIMATION = "checklist-animation"

/**
 * An mvp presenter for the [SplashActivity].
 */
@ActivityScope
class SplashPresenter @Inject constructor() :
    SimplePresenter<SplashView>() {

    private lateinit var splashView: SplashView
    private lateinit var splashActivity: SplashActivity
    private lateinit var commonNavViewModel: CommonNavViewModel
    private lateinit var destination: Class<*>

    private val checklist = Checklist {
        splashView.startActivityAsRoot(destination)
    }

    override fun setView(view: SplashView) {
        splashView = view
        splashActivity = splashView as SplashActivity
    }

    fun setNavigationViewModel(commonNavViewModel: CommonNavViewModel) {
        this.commonNavViewModel = commonNavViewModel
    }

    override fun start(savedInstanceState: Bundle?) {
        checklist.register(CHECKLIST_DESTINATION)
        checklist.register(CHECKLIST_ANIMATION)
        playAnimations()
        commonNavViewModel.destination.observe(splashActivity) {
            if (it != null) {
                destination = it
                checklist.check(CHECKLIST_DESTINATION)
            }
        }

        commonNavViewModel.state.observe(splashActivity) {
            splashView.setAppErrorViewer(
                ToastErrorViewer(
                    commonNavViewModel.appError,
                    splashActivity
                )
            )
            handleLoadingState(splashView, it)
        }
        NetworkState.isConnected.observe(splashActivity) {
            commonNavViewModel.calculateDestination()
        }
    }

    private fun playAnimations() {
        splashView.scaleAppLogo {
            splashView.hideFirstLogo()
            splashView.showSecondLogo()
            splashView.scaleCircle1 {
                splashView.scaleAppLogo2()
                splashView.scaleCircle2 {
                    splashView.squeezeAppLogo()
                    splashView.scaleCircle3 {
                        checklist.check(CHECKLIST_ANIMATION)
                    }
                }
            }
        }
    }
}