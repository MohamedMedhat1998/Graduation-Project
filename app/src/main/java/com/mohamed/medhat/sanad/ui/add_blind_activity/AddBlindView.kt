package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.net.Uri
import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for [AddBlindActivity].
 */
interface AddBlindView : BaseView, LoadingFeatureOwner {
    fun updateProfilePreviewImage(uriImage: Uri?)
    fun getOtherIllness(): String
    fun clearOtherIllness()
    fun getName(): String
    fun getAge(): Int
    fun getGender(): Int
    fun getBloodType(): String
    fun getIllnesses(): List<String>
}