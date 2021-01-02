package com.mohamed.medhat.graduation_project.ui.splash_activity

import com.mohamed.medhat.graduation_project.ui.base.BaseView

/**
 * An mvp view for the [SplashActivity].
 */
interface SplashView : BaseView {
    fun playRotationAnimation(
        id: Int,
        rotation: Float = 360f,
        duration: Long = 1000,
        onRotationFinished: () -> Unit
    )
}