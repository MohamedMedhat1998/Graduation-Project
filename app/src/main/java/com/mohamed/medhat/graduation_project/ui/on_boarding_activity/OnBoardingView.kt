package com.mohamed.medhat.graduation_project.ui.on_boarding_activity

import com.mohamed.medhat.graduation_project.model.OnBoardingCard
import com.mohamed.medhat.graduation_project.ui.base.BaseView

/**
 * An mvp view for the [OnBoardingActivity].
 */
interface OnBoardingView : BaseView {
    /**
     * Loads the [OnBoardingCard]s into the ViewPager inside the [OnBoardingActivity].
     * @param cards the cards to be displayed.
     */
    fun loadCards(cards: List<OnBoardingCard>)
}