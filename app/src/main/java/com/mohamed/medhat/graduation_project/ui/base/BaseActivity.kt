package com.mohamed.medhat.graduation_project.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohamed.medhat.graduation_project.dagger.AppComponent
import com.mohamed.medhat.graduation_project.dagger.DaggerApplication

/**
 * A base class for all the activities in the app. This class is meant to reduce the boilerplate code for the activity classes.
 */
open class BaseActivity<V : BaseView> : AppCompatActivity() {
    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (application as DaggerApplication).appComponent
        super.onCreate(savedInstanceState)
    }

    /**
     * Used to initialize the boilerplate code that an activity's presenter needs.
     * @param presenter the presenter of the activity.
     */
    @Suppress("UNCHECKED_CAST")
    fun initPresenter(presenter: BasePresenter<V>) {
        presenter.setView(this as V)
    }
}