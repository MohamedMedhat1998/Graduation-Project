package com.mohamed.medhat.graduation_project.ui.on_boarding_activity

import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.model.OnBoardingCard
import javax.inject.Inject

/**
 * The source class of the [OnBoardingCard]s that will be used in [OnBoardingActivity].
 */
@ActivityScope
class OnBoardingCardsProvider @Inject constructor() {
    // TODO use a real data
    val cards = listOf(
        OnBoardingCard(android.R.drawable.star_big_on, "Feature one"),
        OnBoardingCard(android.R.drawable.sym_def_app_icon, "Feature two"),
        OnBoardingCard(R.mipmap.ic_launcher_round, "Feature three")
    )
}