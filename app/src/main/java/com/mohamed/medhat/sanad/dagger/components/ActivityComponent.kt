package com.mohamed.medhat.sanad.dagger.components

import com.mohamed.medhat.sanad.dagger.ViewModelFactory
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.ui.confirmation_activity.ConfirmationActivity
import com.mohamed.medhat.sanad.ui.confirmation_activity.ConfirmationViewModel
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.login_activity.LoginNavViewModel
import com.mohamed.medhat.sanad.ui.login_activity.LoginViewModel
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.main_activity.MainViewModel
import com.mohamed.medhat.sanad.ui.on_boarding_activity.OnBoardingActivity
import com.mohamed.medhat.sanad.ui.registration_activity.RegistrationActivity
import com.mohamed.medhat.sanad.ui.registration_activity.RegistrationViewModel
import com.mohamed.medhat.sanad.ui.splash_activity.SplashActivity
import com.mohamed.medhat.sanad.ui.splash_activity.SplashNavViewModel
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
    fun loginNavViewModel(): ViewModelFactory<LoginNavViewModel>
    fun registrationViewModel(): ViewModelFactory<RegistrationViewModel>
    fun mainViewModel(): ViewModelFactory<MainViewModel>
    fun confirmationViewModel(): ViewModelFactory<ConfirmationViewModel>
    fun splashNavViewModel(): ViewModelFactory<SplashNavViewModel>
}