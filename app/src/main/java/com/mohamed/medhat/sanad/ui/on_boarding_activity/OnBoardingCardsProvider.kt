package com.mohamed.medhat.sanad.ui.on_boarding_activity

import android.content.Context
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.OnBoardingCard
import javax.inject.Inject

/**
 * The source class of the [OnBoardingCard]s that will be used in [OnBoardingActivity].
 */
@ActivityScope
class OnBoardingCardsProvider @Inject constructor(val context: Context) {
    val cards = listOf(
        OnBoardingCard(R.drawable.on_boarding_1,
            context.getString(R.string.on_boarding_message_1)
        ),
        OnBoardingCard(R.drawable.on_boarding_2, context.getString(R.string.on_boarding_message_2)),
        OnBoardingCard(R.drawable.on_boarding_3, context.getString(R.string.on_boarding_message_3))
    )
}