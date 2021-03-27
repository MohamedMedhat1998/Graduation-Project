package com.mohamed.medhat.sanad.ui.custom_listeners

import android.animation.Animator

/**
 * A custom animator listener listener that only exposes [onAnimationEnd] function.
 */
interface AnimationEndListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator?) {}
}