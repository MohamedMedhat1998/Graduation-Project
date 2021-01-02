package com.mohamed.medhat.graduation_project.ui.animation

import android.animation.Animator

/**
 * A custom listener that only exposes [onAnimationEnd] function.
 */
interface AnimationEndListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator?) {}
}