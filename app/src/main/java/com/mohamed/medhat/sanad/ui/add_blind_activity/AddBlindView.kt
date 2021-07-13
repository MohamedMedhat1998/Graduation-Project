package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.graphics.Bitmap
import android.net.Uri
import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for [AddBlindActivity].
 */
interface AddBlindView : BaseView, LoadingFeatureOwner {
    fun updateProfilePreviewImage(uriImage: Uri?)
    fun updateProfilePreviewImage(bitmapImage: Bitmap?)
    fun getOtherIllness(): String
    fun clearOtherIllness()
    fun getName(): String
    fun getLastName(): String
    fun getAge(): Int
    fun getPhoneNumber(): String
    fun getGender(): Int
    fun getBloodType(): String
    fun getIllnesses(): List<String>
    fun startAddPictureAnimation()
    fun reverseAddPictureAnimation()
    fun getAddPictureAnimationProgress(): Float
}