package com.mohamed.medhat.graduation_project.dagger.components

import com.mohamed.medhat.graduation_project.dagger.ViewModelFactory
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.confirmation_activity.ConfirmationActivity
import com.mohamed.medhat.graduation_project.ui.confirmation_activity.ConfirmationViewModel
import com.mohamed.medhat.graduation_project.ui.login_activity.LoginActivity
import com.mohamed.medhat.graduation_project.ui.login_activity.LoginViewModel
import com.mohamed.medhat.graduation_project.ui.main_activity.MainActivity
import com.mohamed.medhat.graduation_project.ui.main_activity.MainViewModel
import com.mohamed.medhat.graduation_project.ui.on_boarding_activity.OnBoardingActivity
import com.mohamed.medhat.graduation_project.ui.registration_activity.RegistrationActivity
import com.mohamed.medhat.graduation_project.ui.registration_activity.RegistrationViewModel
import com.mohamed.medhat.graduation_project.ui.splash_activity.SplashActivity
import dagger.Subcomponent

/**
 * A dagger sub-component for the activity lifecycle.
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
    fun inject(onBoardingActivity: OnBoardingActivity)
    fun inject(registrationActivity: RegistrationActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(confirmationActivity: ConfirmationActivity)

    fun loginViewModel(): ViewModelFactory<LoginViewModel>
    fun registrationViewModel(): ViewModelFactory<RegistrationViewModel>
    fun mainViewModel(): ViewModelFactory<MainViewModel>
    fun confirmationViewModel(): ViewModelFactory<ConfirmationViewModel>
}