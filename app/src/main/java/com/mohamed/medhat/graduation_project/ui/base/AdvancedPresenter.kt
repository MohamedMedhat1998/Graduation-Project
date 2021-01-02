package com.mohamed.medhat.graduation_project.ui.base

import androidx.lifecycle.ViewModel

/**
 * A presenter class of type [BasePresenter] that has both a [BaseView] and a [ViewModel].
 */
abstract class AdvancedPresenter<T : BaseView, VM : ViewModel>
    : BasePresenter(), HasView<T>, HasViewModel<VM>