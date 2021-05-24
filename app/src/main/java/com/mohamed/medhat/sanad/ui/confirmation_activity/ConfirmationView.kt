package com.mohamed.medhat.sanad.ui.confirmation_activity

import android.widget.EditText
import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for [ConfirmationActivity].
 */
interface ConfirmationView : BaseView, LoadingFeatureOwner {
    fun getConfirmationCode(): String
    fun getConfirmationViews(): List<EditText>
    fun updateWaitTime(secondsLeft: Int)
    fun hideWaitTime()
    fun showWaitTime()
}