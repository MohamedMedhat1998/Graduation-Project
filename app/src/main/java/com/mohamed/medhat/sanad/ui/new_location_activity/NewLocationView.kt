package com.mohamed.medhat.sanad.ui.new_location_activity

import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

interface NewLocationView : BaseView, LoadingFeatureOwner {
    fun getPlaceName(): String
    fun getAssociatedPersonName(): String
    fun getAssociatedPersonNumber(): String
    fun getPLaceDescription(): String
}