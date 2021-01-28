package com.mohamed.medhat.graduation_project.ui.base.network_state_awareness

import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.networking.NetworkState
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
import com.mohamed.medhat.graduation_project.utils.NETWORK_STATE_AWARENESS

/**
 * A behavior that will freeze the app when the network is unavailable.
 */
class NetworkStateAware : NetworkStateAwareness {

    lateinit var internetAlertDialog: AlertDialog

    override fun handleNetworkStateChangeBehavior(activity: BaseActivity) {
        Log.d(NETWORK_STATE_AWARENESS, "${activity.javaClass.simpleName} is aware")
        NetworkState.isConnected.observe(activity) {
            if (!it) {
                if (!::internetAlertDialog.isInitialized) {
                    internetAlertDialog = AlertDialog.Builder(activity)
                        .setTitle(activity.getString(R.string.no_internet_title))
                        .setMessage(activity.getString(R.string.no_internet_message))
                        .setCancelable(false)
                        .create()
                }
                internetAlertDialog.show()
            } else {
                if (::internetAlertDialog.isInitialized && internetAlertDialog.isShowing) {
                    internetAlertDialog.dismiss()
                }
            }
        }
    }
}