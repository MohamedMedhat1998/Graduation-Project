package com.mohamed.medhat.graduation_project.ui.on_boarding_activity

import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.SimplePresenter
import javax.inject.Inject

/**
 * An mvp presenter for [OnBoardingActivity].
 */
@ActivityScope
class OnBoardingPresenter @Inject constructor(private val onBoardingCardsProvider: OnBoardingCardsProvider) :
    SimplePresenter<OnBoardingView>() {

    private lateinit var onBoardingView: OnBoardingView

    override fun start() {
        onBoardingView.loadCards(onBoardingCardsProvider.cards)
    }

    override fun setView(view: OnBoardingView) {
        onBoardingView = view
    }
}