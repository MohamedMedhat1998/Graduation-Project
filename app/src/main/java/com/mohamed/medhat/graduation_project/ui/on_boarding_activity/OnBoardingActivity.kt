package com.mohamed.medhat.graduation_project.ui.on_boarding_activity

import android.os.Bundle
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.model.OnBoardingCard
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_on_boarding.*
import javax.inject.Inject

/**
 * An activity for the on boarding screen that will contain overview to the user about the app.
 */
class OnBoardingActivity : BaseActivity(), OnBoardingView {

    @Inject
    lateinit var presenter: OnBoardingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        activityComponent.inject(this)
        presenter.setView(this)
        presenter.start()
    }

    override fun loadCards(cards: List<OnBoardingCard>) {
        vp_on_boarding_cards.adapter = OnBoardingViewPagerAdapter(this, cards)
    }
}