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
    // TODO use real images
    val cards = listOf(
        OnBoardingCard(
            android.R.drawable.star_big_on,
            context.getString(R.string.on_boarding_message_1)
        ),
        OnBoardingCard(android.R.drawable.sym_def_app_icon, context.getString(R.string.on_boarding_message_2)),
        OnBoardingCard(R.mipmap.ic_launcher_round, context.getString(R.string.on_boarding_message_3))
    )
}