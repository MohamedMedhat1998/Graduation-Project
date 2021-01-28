package com.mohamed.medhat.graduation_project.ui.base.network_state_awareness

import android.util.Log
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
import com.mohamed.medhat.graduation_project.utils.NETWORK_STATE_AWARENESS

/**
 * A behavior that does nothing when the network state changes.
 */
class NetworkStateUnaware : NetworkStateAwareness {
    override fun handleNetworkStateChangeBehavior(activity: BaseActivity) {
        Log.d(NETWORK_STATE_AWARENESS,"${activity.javaClass.simpleName} is unaware")
        // Do nothing when the network state changes
    }
}