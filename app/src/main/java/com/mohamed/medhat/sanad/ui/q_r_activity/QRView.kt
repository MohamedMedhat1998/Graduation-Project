package com.mohamed.medhat.sanad.ui.q_r_activity

import com.mohamed.medhat.sanad.ui.base.BaseView

/**
 * An mvp view for the [QRActivity].
 */
interface QRView : BaseView {
    fun updateHelloMessage(message: String)
}