package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.net.Uri
import com.mohamed.medhat.sanad.ui.base.BaseView

/**
 * An mvp view for [AddBlindActivity].
 */
interface AddBlindView : BaseView {
    fun updateProfilePreviewImage(uriImage: Uri?)
}