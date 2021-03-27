package com.mohamed.medhat.sanad.ui.base

/**
 * A presenter class of type [BasePresenter] that has only [BaseView].
 */
abstract class SimplePresenter<T : BaseView> : BasePresenter(), HasView<T>