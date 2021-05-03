package com.mohamed.medhat.sanad.ui.main_activity

import com.google.android.gms.maps.OnMapReadyCallback
import com.mohamed.medhat.sanad.ui.base.BaseView

/**
 * An mvp view for the main screen.
 */
interface MainView : BaseView, OnMapReadyCallback {
    /**
     * Adds a pin on the map.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun addPin(x: Double, y: Double)

    /**
     * Adds a pin on the map.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param title The title of the pin.
     */
    fun addPin(x: Double, y: Double, title: String)
}