package com.mohamed.medhat.graduation_project.ui.confirmation_activity

import com.mohamed.medhat.graduation_project.ui.base.BaseView

/**
 * An mvp view for [ConfirmationActivity].
 */
interface ConfirmationView : BaseView {
    fun getConfirmationCode(): String
}