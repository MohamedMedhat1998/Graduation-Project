package com.mohamed.medhat.graduation_project.ui.base

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mohamed.medhat.graduation_project.dagger.DaggerApplication
import com.mohamed.medhat.graduation_project.dagger.components.ActivityComponent
import com.mohamed.medhat.graduation_project.ui.base.error_viewers.AppErrorViewer
import com.mohamed.medhat.graduation_project.ui.base.error_viewers.NoErrorViewer
import com.mohamed.medhat.graduation_project.ui.base.network_state_awareness.NetworkStateAware
import com.mohamed.medhat.graduation_project.ui.base.network_state_awareness.NetworkStateAwareness

/**
 * A base class for all the activities in the app. This class is meant to reduce the boilerplate code for the activity classes.
 */
open class BaseActivity : AppCompatActivity(), BaseView {
    lateinit var activityComponent: ActivityComponent
    private var networkStateAwareness: NetworkStateAwareness = NetworkStateAware()
    private var appErrorViewer: AppErrorViewer = NoErrorViewer()

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent =
            (application as DaggerApplication).appComponent.activityComponent().create()
        super.onCreate(savedInstanceState)
        networkStateAwareness.handleNetworkStateChangeBehavior(this)
    }

    override fun navigateTo(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }

    override fun navigateToThenFinish(activity: Class<*>) {
        navigateTo(activity)
        finish()
    }

    override fun displayToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun showInputError(editText: EditText, error: String) {
        editText.error = error
    }

    override fun resetInputError(editText: EditText) {
        editText.error = null
    }

    override fun setNetworkStateAwareness(networkStateAwareness: NetworkStateAwareness) {
        this.networkStateAwareness = networkStateAwareness
    }

    override fun setAppErrorViewer(appErrorViewer: AppErrorViewer) {
        this.appErrorViewer = appErrorViewer
    }

    override fun displayAppError() {
        appErrorViewer.display()
    }

    override fun hideAppError() {
        appErrorViewer.hide()
    }
}