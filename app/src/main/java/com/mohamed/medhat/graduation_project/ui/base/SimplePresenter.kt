package com.mohamed.medhat.graduation_project.ui.base

/**
 * A presenter class of type [BasePresenter] that has only [BaseView].
 */
abstract class SimplePresenter<T : BaseView> : BasePresenter(), HasView<T>