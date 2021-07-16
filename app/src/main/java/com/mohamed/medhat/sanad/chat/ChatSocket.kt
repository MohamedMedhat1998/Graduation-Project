package com.mohamed.medhat.sanad.chat

import android.util.Log
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnection
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import javax.inject.Inject

/**
 * A class responsible for establishing the web-socket connection with the back-end to receive chat messages.
 */
@ActivityScope
class ChatSocket @Inject constructor(private val connection: HubConnection) {

    fun startListening(
        onMessageReceived: (HubMessage) -> Unit,
        onConnectionMessageReceived: (HubMessage?) -> Unit
    ) {
        registerConnectionListener(onConnectionMessageReceived)
        connection.subscribeToEvent("ReceiveMessageAsMobile") {
            onMessageReceived.invoke(it)
            Log.d("ChatMessage", "Received Something")
            it.arguments.forEachIndexed { index, element ->
                Log.d("ChatMessage#$index", element.toString())
            }
        }
    }

    private fun registerConnectionListener(onConnectionMessageReceived: (HubMessage?) -> Unit) {
        val connectionListener = object : HubConnectionListener {
            override fun onConnected() {
                Log.d("Connection", "Connected")
            }

            override fun onDisconnected() {
                Log.d("Connection", "Disconnected")
            }

            override fun onMessage(message: HubMessage?) {
                Log.d("Connection", "Message")
                Log.d("ConnectionMessage", message.toString())
                message?.arguments?.forEachIndexed { index, element ->
                    Log.d("ConnectionMessage#$index", element.toString())
                }
                onConnectionMessageReceived.invoke(message)
            }

            override fun onError(exception: Exception?) {
                Log.e("Connection", "Error")
                exception?.printStackTrace()
            }
        }
        connection.addListener(connectionListener)
    }

    /**
     * Starts the web-socket connection.
     */
    fun connect() {
        connection.connect()
    }

    /**
     * Ends the web-socket connection.
     */
    fun disconnect() {
        connection.disconnect()
    }
}