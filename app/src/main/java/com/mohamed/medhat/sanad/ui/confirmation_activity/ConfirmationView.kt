package com.mohamed.medhat.sanad.ui.confirmation_activity

import com.mohamed.medhat.sanad.ui.base.BaseView

/**
 * An mvp view for [ConfirmationActivity].
 */
interface ConfirmationView : BaseView {
    fun getConfirmationCode(): String
}