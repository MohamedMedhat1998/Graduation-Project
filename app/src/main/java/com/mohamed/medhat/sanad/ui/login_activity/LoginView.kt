package com.mohamed.medhat.sanad.ui.login_activity

import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for [LoginActivity].
 */
interface LoginView : BaseView, LoadingFeatureOwner {
    fun getEmail(): String
    fun getPassword(): String
}