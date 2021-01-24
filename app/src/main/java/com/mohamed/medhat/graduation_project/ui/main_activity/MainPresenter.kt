package com.mohamed.medhat.graduation_project.ui.main_activity

import android.os.Bundle
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import javax.inject.Inject

/**
 * An mvp presenter for the main screen.
 */
@ActivityScope
class MainPresenter @Inject constructor() : AdvancedPresenter<MainView, MainViewModel>() {

    private lateinit var mainView: MainView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var activity: MainActivity

    override fun start(savedInstanceState: Bundle?) {
        activity = mainView as MainActivity
    }

    override fun setView(view: MainView) {
        mainView = view
    }

    override fun setViewModel(viewModel: MainViewModel) {
        mainViewModel = viewModel
    }
}