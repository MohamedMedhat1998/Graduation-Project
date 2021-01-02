package com.mohamed.medhat.graduation_project.ui.base

import androidx.lifecycle.ViewModel

/**
 * An interface that is meant to be implemented by presenters that should contain a [ViewModel] object.
 */
interface HasViewModel<VM : ViewModel> {
    fun setViewModel(viewModel: VM)
}