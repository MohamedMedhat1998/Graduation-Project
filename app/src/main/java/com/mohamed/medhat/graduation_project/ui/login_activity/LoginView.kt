package com.mohamed.medhat.graduation_project.ui.login_activity

import com.mohamed.medhat.graduation_project.ui.base.BaseView

/**
 * An mvp view for [LoginActivity].
 */
interface LoginView : BaseView {
    fun getEmail(): String
    fun getPassword(): String
}