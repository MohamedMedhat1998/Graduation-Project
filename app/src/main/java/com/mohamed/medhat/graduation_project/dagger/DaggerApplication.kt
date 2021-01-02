package com.mohamed.medhat.graduation_project.dagger

import android.app.Application
import com.mohamed.medhat.graduation_project.dagger.components.AppComponent
import com.mohamed.medhat.graduation_project.dagger.components.DaggerAppComponent

/**
 * A dagger application.
 */
class DaggerApplication : Application() {
    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }

}