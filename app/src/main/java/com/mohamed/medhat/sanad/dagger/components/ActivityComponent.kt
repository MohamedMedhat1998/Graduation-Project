package com.mohamed.medhat.sanad.dagger.components

import com.mohamed.medhat.sanad.dagger.ViewModelFactory
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.ui.CommonNavViewModel
import com.mohamed.medhat.sanad.ui.add_blind_activity.AddBlindActivity
import com.mohamed.medhat.sanad.ui.add_blind_activity.AddBlindViewModel
import com.mohamed.medhat.sanad.ui.add_person_activity.AddPersonActivity
import com.mohamed.medhat.sanad.ui.add_person_activity.AddPersonViewModel
import com.mohamed.medhat.sanad.ui.confirmation_activity.ConfirmationActivity
import com.mohamed.medhat.sanad.ui.confirmation_activity.ConfirmationViewModel
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.login_activity.LoginNavViewModel
import com.mohamed.medhat.sanad.ui.login_activity.LoginViewModel
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.main_activity.MainViewModel
import com.mohamed.medhat.sanad.ui.mentor_picture_activity.MentorPictureActivity
import com.mohamed.medhat.sanad.ui.mentor_picture_activity.MentorPictureNavViewModel
import com.mohamed.medhat.sanad.ui.mentor_picture_activity.MentorPictureViewModel
import com.mohamed.medhat.sanad.ui.new_location_activity.NewLocationActivity
import com.mohamed.medhat.sanad.ui.new_location_activity.NewLocationPresenter
import com.mohamed.medhat.sanad.ui.new_location_activity.NewLocationViewModel
import com.mohamed.medhat.sanad.ui.on_boarding_activity.OnBoardingActivity
import com.mohamed.medhat.sanad.ui.persons_manager_activity.PersonsManagerActivity
import com.mohamed.medhat.sanad.ui.persons_manager_activity.PersonsManagerViewModel
import com.mohamed.medhat.sanad.ui.places_activity.PlacesActivity
import com.mohamed.medhat.sanad.ui.places_activity.PlacesViewModel
import com.mohamed.medhat.sanad.ui.q_r_activity.QRActivity
import com.mohamed.medhat.sanad.ui.q_r_activity.scanner.ScannerActivity
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
    fun inject(qrActivity: QRActivity)
    fun inject(scannerActivity: ScannerActivity)
    fun inject(addBlindActivity: AddBlindActivity)
    fun inject(personsManagerActivity: PersonsManagerActivity)
    fun inject(addPersonActivity: AddPersonActivity)
    fun inject(mentorPictureActivity: MentorPictureActivity)
    fun inject(placesActivity: PlacesActivity)
    fun inject(newLocationActivity: NewLocationActivity)

    fun loginViewModel(): ViewModelFactory<LoginViewModel>
    fun loginNavViewModel(): ViewModelFactory<LoginNavViewModel>
    fun registrationViewModel(): ViewModelFactory<RegistrationViewModel>
    fun mainViewModel(): ViewModelFactory<MainViewModel>
    fun confirmationViewModel(): ViewModelFactory<ConfirmationViewModel>
    fun splashNavViewModel(): ViewModelFactory<SplashNavViewModel>
    fun addBlindViewModel(): ViewModelFactory<AddBlindViewModel>
    fun personsManagerViewModel(): ViewModelFactory<PersonsManagerViewModel>
    fun addPersonViewModel(): ViewModelFactory<AddPersonViewModel>
    fun mentorPictureViewModel(): ViewModelFactory<MentorPictureViewModel>
    fun mentorPictureNavViewModel(): ViewModelFactory<MentorPictureNavViewModel>
    fun commonNavViewModel(): ViewModelFactory<CommonNavViewModel>
    fun placesViewModel(): ViewModelFactory<PlacesViewModel>
    fun newLocationViewModel(): ViewModelFactory<NewLocationViewModel>
}