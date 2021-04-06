package com.mohamed.medhat.sanad.ui.add_blind_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import javax.inject.Inject

/**
 * An mvp presenter for [AddBlindActivity].
 */
class AddBlindPresenter @Inject constructor() : AdvancedPresenter<AddBlindView, AddBlindViewModel>() {

    private lateinit var addBlindView: AddBlindView
    private lateinit var addBlindViewModel: AddBlindViewModel

    override fun start(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun setView(view: AddBlindView) {
        addBlindView = view
    }

    override fun setViewModel(viewModel: AddBlindViewModel) {
        addBlindViewModel = viewModel
    }
}