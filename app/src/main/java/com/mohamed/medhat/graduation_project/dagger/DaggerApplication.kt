package com.mohamed.medhat.graduation_project.dagger

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.widget.Toast
import com.mohamed.medhat.graduation_project.dagger.components.AppComponent
import com.mohamed.medhat.graduation_project.dagger.components.DaggerAppComponent
import com.mohamed.medhat.graduation_project.networking.NetworkState

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

    override fun onCreate() {
        super.onCreate()
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()
        val connectivityManager = (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager)
        connectivityManager.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    NetworkState.isConnected = true
                    Toast.makeText(this@DaggerApplication, "Connected", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    NetworkState.isConnected = false
                    Toast.makeText(this@DaggerApplication, "Disconnected", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}