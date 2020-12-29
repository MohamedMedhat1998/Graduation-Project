package com.mohamed.medhat.graduation_project.dagger

import android.content.Context
import com.mohamed.medhat.graduation_project.ui.login_activity.LoginActivity
import com.mohamed.medhat.graduation_project.ui.splash_activity.SplashActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * A dagger component which is used to trigger the injection for the required activities.
 */
@Singleton
@Component(modules = [ViewModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        // Use @BindsInstance for objects that are constructed outside of the graph (e.g. instances of Context).
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(loginActivity: LoginActivity)
    fun inject(splashActivity: SplashActivity)
}