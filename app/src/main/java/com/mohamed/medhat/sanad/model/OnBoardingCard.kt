package com.mohamed.medhat.sanad.model

import com.mohamed.medhat.sanad.ui.on_boarding_activity.OnBoardingActivity
import java.io.Serializable

/**
 * A data class for the "on boarding" objects inside the [OnBoardingActivity].
 * @param imageId the id of the image that will represent a specific feature of the app.
 * @param text a short text that describes a specific feature of the app.
 * @param isTheLastCard indicates whether the current card is the last one in the list or not.
 */
data class OnBoardingCard(val imageId: Int, val text: String, val isTheLastCard: Boolean = false) :
    Serializable