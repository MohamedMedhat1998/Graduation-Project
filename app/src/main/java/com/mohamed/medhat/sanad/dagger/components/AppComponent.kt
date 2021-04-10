package com.mohamed.medhat.sanad.dagger.components

import android.content.Context
import com.mohamed.medhat.sanad.dagger.modules.AppSubcomponentsModule
import com.mohamed.medhat.sanad.dagger.modules.IllnessesModule
import com.mohamed.medhat.sanad.dagger.modules.NetworkModule
import com.mohamed.medhat.sanad.dagger.modules.SharedPreferencesModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * A dagger component for the app lifecycle.
 */
@Singleton
@Component(modules = [AppSubcomponentsModule::class, NetworkModule::class, SharedPreferencesModule::class, IllnessesModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        // Use @BindsInstance for objects that are constructed outside of the graph (e.g. instances of Context).
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun activityComponent(): ActivityComponent.Factory
}