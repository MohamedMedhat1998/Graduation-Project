package com.mohamed.medhat.graduation_project.ui.splash_activity

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.ui.animation.AnimationEndListener
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
import javax.inject.Inject

/**
 * An activity for the splash screen.
 */
class SplashActivity : BaseActivity(), SplashView {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        activityComponent.inject(this)
        splashPresenter.setView(this)
        splashPresenter.start(savedInstanceState)
    }

    override fun playRotationAnimation(
        id: Int,
        rotation: Float,
        duration: Long,
        onRotationFinished: () -> Unit
    ) {
        val animationListener = object : AnimationEndListener {
            override fun onAnimationEnd(animation: Animator?) {
                onRotationFinished.invoke()
            }
        }
        findViewById<View>(id).animate()
            .rotation(rotation)
            .setDuration(duration)
            .setListener(animationListener)
            .start()
    }
}