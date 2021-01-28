package com.mohamed.medhat.graduation_project.dagger

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import com.mohamed.medhat.graduation_project.dagger.components.AppComponent
import com.mohamed.medhat.graduation_project.dagger.components.DaggerAppComponent
import com.mohamed.medhat.graduation_project.networking.NetworkState
import com.mohamed.medhat.graduation_project.utils.INTERNET

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
        var availableFlag = false
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()
        val connectivityManager = (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager)
        connectivityManager.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    NetworkState.isConnected.postValue(true)
                    Log.d(INTERNET, "Connected!")
                    availableFlag = true
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    NetworkState.isConnected.postValue(false)
                    Log.d(INTERNET, "disconnected!")
                }
            })
        // Fixing the initial state of the "No Internet Connection" state.
        Thread {
            Thread.sleep(100)
            if (!availableFlag) {
                Log.d(INTERNET, "disconnected!")
                NetworkState.isConnected.postValue(false)
            }
        }.start()
    }
}