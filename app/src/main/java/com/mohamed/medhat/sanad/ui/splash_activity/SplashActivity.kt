package com.mohamed.medhat.sanad.ui.splash_activity

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateUnaware
import com.mohamed.medhat.sanad.ui.custom_listeners.AnimationEndListener
import javax.inject.Inject

/**
 * An activity for the splash screen.
 */
class SplashActivity : BaseActivity(), SplashView {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    val splashNavViewModel: SplashNavViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.splashNavViewViewModel())
            .get(SplashNavViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setNetworkStateAwareness(NetworkStateUnaware())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        activityComponent.inject(this)
        splashPresenter.setView(this)
        splashPresenter.setSplashNaveViewModel(splashNavViewModel)
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

    override fun showLoadingIndicator() {
        // TODO Blocked on UI
    }

    override fun hideLoadingIndicator() {
        // TODO Blocked on UI
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }
}