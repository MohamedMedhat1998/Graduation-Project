package com.mohamed.medhat.sanad.ui.on_boarding_activity

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.OnBoardingCard
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateUnaware
import kotlinx.android.synthetic.main.activity_on_boarding.*
import javax.inject.Inject

/**
 * An activity for the on boarding screen that will contain overview to the user about the app.
 */
class OnBoardingActivity : BaseActivity(), OnBoardingView {

    @Inject
    lateinit var presenter: OnBoardingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setNetworkStateAwareness(NetworkStateUnaware())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        activityComponent.inject(this)
        presenter.setView(this)
        presenter.start(savedInstanceState)
        btn_on_boarding_next.setOnClickListener {
            presenter.onNextClicked()
        }
        btn_on_boarding_skip.setOnClickListener {
            presenter.onSkipClicked()
        }
    }

    override fun loadCards(cards: List<OnBoardingCard>) {
        vp_on_boarding_cards.adapter = OnBoardingViewPagerAdapter(this, cards)
        TabLayoutMediator(tl_on_boarding_indicator, vp_on_boarding_cards) { _, _ -> }.attach()
    }

    override fun setCurrentItem(item: Int) {
        vp_on_boarding_cards.setCurrentItem(item, true)
    }

    override fun getCurrentItem(): Int {
        return vp_on_boarding_cards.currentItem
    }

    override fun getCardsCount(): Int {
        return vp_on_boarding_cards.adapter!!.itemCount
    }
}