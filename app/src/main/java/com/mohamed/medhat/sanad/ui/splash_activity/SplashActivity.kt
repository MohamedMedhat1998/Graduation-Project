package com.mohamed.medhat.sanad.ui.splash_activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.CommonNavViewModel
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateUnaware
import com.mohamed.medhat.sanad.ui.custom_listeners.AnimationEndListener
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

/**
 * An activity for the splash screen.
 */
class SplashActivity : BaseActivity(), SplashView {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    private val commonNavViewModel: CommonNavViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.commonNavViewModel())
            .get(CommonNavViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setNetworkStateAwareness(NetworkStateUnaware())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        activityComponent.inject(this)
        splashPresenter.setView(this)
        splashPresenter.setNavigationViewModel(commonNavViewModel)
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

    override fun scaleAppLogo(onComplete: () -> Unit) {
        iv_app_logo.animate()
            .scaleX(1.45f)
            .scaleY(1.45f)
            .setDuration(800)
            .start()
        val animator = iv_app_logo2.animate()
            .scaleX(1.45f)
            .scaleY(1.45f)
            .setDuration(800)
        animator.setListener(object : AnimationEndListener {
            override fun onAnimationEnd(animation: Animator?) {
                animator.setListener(null)
                onComplete.invoke()
            }
        })
        animator.start()
    }

    override fun scaleCircle1(onComplete: () -> Unit) {
        val animator = iv_splash_circle.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
        animator.setListener(object : AnimationEndListener {
            override fun onAnimationEnd(animation: Animator?) {
                animator.setListener(null)
                onComplete.invoke()
            }
        })
        animator.start()
    }

    override fun hideFirstLogo() {
        val hide = ValueAnimator.ofFloat(1f, 0f)
        hide.addUpdateListener {
            iv_app_logo.alpha = it.animatedValue as Float
        }
        hide.duration = 600
        hide.start()
    }

    override fun showSecondLogo() {
        val show = ValueAnimator.ofFloat(0f, 1f)
        show.addUpdateListener {
            iv_app_logo2.alpha = it.animatedValue as Float
        }
        show.duration = 600
        show.start()
    }

    override fun scaleCircle2(onComplete: () -> Unit) {
        val animator = iv_splash_circle.animate()
            .scaleXBy(1.25f)
            .scaleYBy(1.25f)
            .setDuration(600)
        animator.setListener(object : AnimationEndListener {
            override fun onAnimationEnd(animation: Animator?) {
                animator.setListener(null)
                onComplete.invoke()
            }
        })
        animator.start()
    }

    override fun scaleAppLogo2() {
        iv_app_logo2.animate()
            .scaleYBy(2f)
            .scaleXBy(2f)
            .setDuration(300)
            .start()
    }

    override fun scaleCircle3(onComplete: () -> Unit) {
        val animator = iv_splash_circle.animate()
            .scaleXBy(2f)
            .scaleYBy(3f)
            .setDuration(600)
        animator.setListener(object : AnimationEndListener {
            override fun onAnimationEnd(animation: Animator?) {
                animator.setListener(null)
                onComplete.invoke()
            }
        })
        animator.start()
    }

    override fun squeezeAppLogo() {
        iv_app_logo2.animate()
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(600)
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