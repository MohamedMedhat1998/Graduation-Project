package com.mohamed.medhat.graduation_project.ui.splash_activity

import android.os.Bundle
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.SimplePresenter
import com.mohamed.medhat.graduation_project.ui.confirmation_activity.ConfirmationActivity
import com.mohamed.medhat.graduation_project.ui.on_boarding_activity.OnBoardingActivity
import com.mohamed.medhat.graduation_project.utils.managers.TokenManager
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * An mvp presenter for the [SplashActivity].
 */
@ActivityScope
class SplashPresenter @Inject constructor(val tokenManager: TokenManager) :
    SimplePresenter<SplashView>() {

    private lateinit var splashView: SplashView

    override fun setView(view: SplashView) {
        splashView = view
    }

    override fun start(savedInstanceState: Bundle?) {
        splashView.playRotationAnimation(R.id.iv_app_logo, onRotationFinished = {
            splashView.playRotationAnimation(
                R.id.tv_app_slogan,
                rotation = 720f,
                onRotationFinished = {
                    if (savedInstanceState == null) {
                        thread {
                            Thread.sleep(1500)
                            // TODO fix the navigation
                            if (tokenManager.getToken().isEmpty()) {
                                splashView.navigateToThenFinish(OnBoardingActivity::class.java)
                            } else {
                                splashView.navigateToThenFinish(ConfirmationActivity::class.java)
                            }
                        }
                    }
                })
        })
    }
}