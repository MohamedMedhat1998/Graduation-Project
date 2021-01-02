package com.mohamed.medhat.graduation_project.ui.base

/**
 * The parent class for all the mvp presenter classes.
 */
abstract class BasePresenter {
    /**
     * A simple function that can be used when the presenter is initialized. Usually called after the [BaseView] is
     * set to the presenter object.
     */
    abstract fun start()
}