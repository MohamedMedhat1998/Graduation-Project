package com.mohamed.medhat.graduation_project.dagger.components

import com.mohamed.medhat.graduation_project.dagger.ViewModelFactory
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.login_activity.LoginActivity
import com.mohamed.medhat.graduation_project.ui.login_activity.LoginViewModel
import com.mohamed.medhat.graduation_project.ui.splash_activity.SplashActivity
import com.mohamed.medhat.graduation_project.ui.splash_activity.SplashViewModel
import dagger.Subcomponent

/**
 * A dagger subcomponent for the activity lifecycle.
 */
@ActivityScope
@Subcomponent
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(loginActivity: LoginActivity)
    fun inject(splashActivity: SplashActivity)

    fun loginViewModel(): ViewModelFactory<LoginViewModel>
    fun splashViewModel(): ViewModelFactory<SplashViewModel>
}