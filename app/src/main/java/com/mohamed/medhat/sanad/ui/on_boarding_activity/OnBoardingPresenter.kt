package com.mohamed.medhat.sanad.ui.on_boarding_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import javax.inject.Inject

/**
 * An mvp presenter for [OnBoardingActivity].
 */
@ActivityScope
class OnBoardingPresenter @Inject constructor(private val onBoardingCardsProvider: OnBoardingCardsProvider) :
    SimplePresenter<OnBoardingView>() {

    private lateinit var onBoardingView: OnBoardingView

    override fun start(savedInstanceState: Bundle?) {
        onBoardingView.loadCards(onBoardingCardsProvider.cards)
    }

    override fun setView(view: OnBoardingView) {
        onBoardingView = view
    }
}