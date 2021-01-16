package com.mohamed.medhat.graduation_project.ui.login_activity

import com.mohamed.medhat.graduation_project.ui.base.BaseView
import com.mohamed.medhat.graduation_project.ui.base.LoadingFeatureOwner

/**
 * An mvp view for [LoginActivity].
 */
interface LoginView : BaseView, LoadingFeatureOwner {
    fun getEmail(): String
    fun getPassword(): String
}