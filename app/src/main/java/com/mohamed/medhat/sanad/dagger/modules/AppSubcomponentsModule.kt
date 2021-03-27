package com.mohamed.medhat.sanad.dagger.modules

import com.mohamed.medhat.sanad.dagger.components.ActivityComponent
import com.mohamed.medhat.sanad.dagger.components.AppComponent
import dagger.Module

/**
 * A module that contains all the subcomponents for the [AppComponent].
 */
@Module(subcomponents = [ActivityComponent::class])
class AppSubcomponentsModule