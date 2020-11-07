package com.mohamed.medhat.myapplication.ui.splash_activity

import com.mohamed.medhat.myapplication.ui.BasePresenter

class SplashPresenter(private val splashView: SplashView) : BasePresenter() {
    lateinit var splashViewModel: SplashViewModel
}