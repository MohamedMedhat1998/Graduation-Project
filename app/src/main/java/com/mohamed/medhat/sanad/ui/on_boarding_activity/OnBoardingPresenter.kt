package com.mohamed.medhat.sanad.ui.on_boarding_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import javax.inject.Inject

/**
 * An mvp presenter for [OnBoardingActivity].
 */
@ActivityScope
class OnBoardingPresenter @Inject constructor(val onBoardingCardsProvider: OnBoardingCardsProvider) :
    SimplePresenter<OnBoardingView>() {

    private lateinit var onBoardingView: OnBoardingView

    override fun start(savedInstanceState: Bundle?) {
        onBoardingView.loadCards(onBoardingCardsProvider.cards)
    }

    override fun setView(view: OnBoardingView) {
        onBoardingView = view
    }

    fun onNextClicked() {
        if (onBoardingView.getCurrentItem() == onBoardingView.getCardsCount() - 1) {
            onBoardingView.navigateToThenFinish(LoginActivity::class.java)
        } else {
            onBoardingView.setCurrentItem(onBoardingView.getCurrentItem() + 1)
        }
    }

    fun onSkipClicked() {
        if (onBoardingView.getCurrentItem() == onBoardingView.getCardsCount() - 1) {
            onBoardingView.navigateToThenFinish(LoginActivity::class.java)
        } else {
            onBoardingView.setCurrentItem(onBoardingView.getCardsCount() - 1)
        }
    }
}