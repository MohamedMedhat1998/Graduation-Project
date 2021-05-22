package com.mohamed.medhat.sanad.ui.mentor_picture_activity

import android.net.Uri
import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for the [MentorPictureActivity].
 */
interface MentorPictureView : BaseView, LoadingFeatureOwner {
    fun updateProfilePicturePreview(image: Uri?)
}