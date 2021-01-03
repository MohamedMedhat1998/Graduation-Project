package com.mohamed.medhat.graduation_project.model

import com.mohamed.medhat.graduation_project.ui.on_boarding_activity.OnBoardingActivity
import java.io.Serializable

/**
 * A data class for the "on boarding" objects inside the [OnBoardingActivity].
 * @param imageId the id of the image that will represent a specific feature of the app.
 * @param text a short text that describes a specific feature of the app.
 */
data class OnBoardingCard(val imageId: Int, val text: String) : Serializable