package com.mohamed.medhat.sanad.ui.persons_manager_activity

import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for the [PersonsManagerView].
 */
interface PersonsManagerView : BaseView, LoadingFeatureOwner {
    fun displayEmptyListHint()
    fun hideEmptyListHint()
}