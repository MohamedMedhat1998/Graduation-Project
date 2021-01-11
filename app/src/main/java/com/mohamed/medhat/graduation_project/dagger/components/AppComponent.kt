package com.mohamed.medhat.graduation_project.dagger.components

import android.content.Context
import com.mohamed.medhat.graduation_project.dagger.modules.AppSubcomponentsModule
import com.mohamed.medhat.graduation_project.dagger.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * A dagger component for the app lifecycle.
 */
@Singleton
@Component(modules = [AppSubcomponentsModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        // Use @BindsInstance for objects that are constructed outside of the graph (e.g. instances of Context).
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun activityComponent(): ActivityComponent.Factory
}