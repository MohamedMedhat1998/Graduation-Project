package com.mohamed.medhat.graduation_project.ui.base

/**
 * The parent class for all the mvp presenter classes.
 * It contains all the common functions among the mvp presenter classes.
 */
abstract class BasePresenter<T: BaseView> {
    abstract fun setView(view: T)
}