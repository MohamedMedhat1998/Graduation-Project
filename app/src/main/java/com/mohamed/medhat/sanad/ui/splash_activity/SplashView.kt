package com.mohamed.medhat.sanad.ui.splash_activity

import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for the [SplashActivity].
 */
interface SplashView : BaseView, LoadingFeatureOwner {
    /**
     * Applies a rotation animation to a specific view.
     * @param id the id of the view to be rotated.
     * @param rotation the rotation angle.
     * @param duration the duration of the animation.
     * @param onRotationFinished a lambda that should be called when the rotation is done.
     */
    fun playRotationAnimation(
        id: Int,
        rotation: Float = 360f,
        duration: Long = 1000,
        onRotationFinished: () -> Unit
    )
    fun scaleAppLogo(onComplete: () -> Unit)
    fun scaleCircle1(onComplete: () -> Unit)
    fun hideFirstLogo()
    fun showSecondLogo()
    fun scaleCircle2(onComplete: () -> Unit)
    fun scaleAppLogo2()
    fun scaleCircle3(onComplete: () -> Unit)
    fun squeezeAppLogo()
}