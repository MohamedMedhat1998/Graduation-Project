package com.mohamed.medhat.sanad.ui.add_person_activity

import android.net.Uri
import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for the [AddPersonActivity].
 */
interface AddPersonView : BaseView, LoadingFeatureOwner {
    fun getName(): String
    fun updatePreviewPicture(imageUri: Uri)
    fun showPreviewPicture()
}