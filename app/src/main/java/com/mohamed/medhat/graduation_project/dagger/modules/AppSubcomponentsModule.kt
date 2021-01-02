package com.mohamed.medhat.graduation_project.dagger.modules

import com.mohamed.medhat.graduation_project.dagger.components.ActivityComponent
import com.mohamed.medhat.graduation_project.dagger.components.AppComponent
import dagger.Module

/**
 * A module that contains all the subcomponents for the [AppComponent].
 */
@Module(subcomponents = [ActivityComponent::class])
class AppSubcomponentsModule