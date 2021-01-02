package com.mohamed.medhat.graduation_project.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohamed.medhat.graduation_project.dagger.DaggerApplication
import com.mohamed.medhat.graduation_project.dagger.components.ActivityComponent

/**
 * A base class for all the activities in the app. This class is meant to reduce the boilerplate code for the activity classes.
 */
open class BaseActivity : AppCompatActivity() {
    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent =
            (application as DaggerApplication).appComponent.activityComponent().create()
        super.onCreate(savedInstanceState)
    }
}