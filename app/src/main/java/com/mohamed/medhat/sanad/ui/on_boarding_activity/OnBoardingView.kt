package com.mohamed.medhat.sanad.ui.on_boarding_activity

import com.mohamed.medhat.sanad.model.OnBoardingCard
import com.mohamed.medhat.sanad.ui.base.BaseView

/**
 * An mvp view for the [OnBoardingActivity].
 */
interface OnBoardingView : BaseView {
    /**
     * Loads the [OnBoardingCard]s into the ViewPager inside the [OnBoardingActivity].
     * @param cards the cards to be displayed.
     */
    fun loadCards(cards: List<OnBoardingCard>)
    fun setCurrentItem(item: Int)
    fun getCurrentItem(): Int
    fun getCardsCount(): Int
}