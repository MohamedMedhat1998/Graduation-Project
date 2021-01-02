package com.mohamed.medhat.graduation_project.ui.base

/**
 * An interface that is meant to be implemented by presenters that should contain a [BaseView] object.
 */
interface HasView<T: BaseView> {
    fun setView(view: T)
}