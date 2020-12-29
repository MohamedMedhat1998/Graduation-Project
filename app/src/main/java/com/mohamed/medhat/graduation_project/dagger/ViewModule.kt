package com.mohamed.medhat.graduation_project.dagger

import com.mohamed.medhat.graduation_project.ui.base.BaseView
import com.mohamed.medhat.graduation_project.ui.login_activity.LoginActivity
import com.mohamed.medhat.graduation_project.ui.login_activity.LoginView
import com.mohamed.medhat.graduation_project.ui.splash_activity.SplashActivity
import com.mohamed.medhat.graduation_project.ui.splash_activity.SplashView
import dagger.Binds
import dagger.Module

/**
 * A dagger module that is used to provide the dependencies that extend the [BaseView] interface.
 */
@Module
abstract class ViewModule {
    @Binds
    abstract fun provideSplashView(splashActivity: SplashActivity): SplashView

    @Binds
    abstract fun provideLoginView(loginActivity: LoginActivity): LoginView
}