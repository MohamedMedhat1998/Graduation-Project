package com.mohamed.medhat.sanad.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * A [BroadcastReceiver] for adding a known person.
 */
class AddKnownPersonReceiver(private val onReceive: (intent: Intent?) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        onReceive.invoke(intent)
    }
}