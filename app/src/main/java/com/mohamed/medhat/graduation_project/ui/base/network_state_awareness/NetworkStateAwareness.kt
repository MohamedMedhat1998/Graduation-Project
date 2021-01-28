package com.mohamed.medhat.graduation_project.ui.base.network_state_awareness

import com.mohamed.medhat.graduation_project.ui.base.BaseActivity

/**
 * The parent of the family algorithms that respond to the network state changes.
 */
interface NetworkStateAwareness {
    /**
     * Responsible for responding to the network state changes.
     * @param activity the activity that follows this behavior.
     */
    fun handleNetworkStateChangeBehavior(activity: BaseActivity)
}