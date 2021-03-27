package com.mohamed.medhat.sanad.ui.on_boarding_activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mohamed.medhat.sanad.model.OnBoardingCard
import com.mohamed.medhat.sanad.ui.on_boarding_activity.fragments.OnBoardingCardFragment

/**
 * An adapter for the on boarding ViewPager in the [OnBoardingActivity].
 */
class OnBoardingViewPagerAdapter(
    activity: AppCompatActivity,
    private val onBoardingCards: List<OnBoardingCard>
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return onBoardingCards.size
    }

    override fun createFragment(position: Int): Fragment =
        OnBoardingCardFragment.newInstance(onBoardingCards[position])
}