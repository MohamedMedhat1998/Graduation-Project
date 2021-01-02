package com.mohamed.medhat.graduation_project.ui.splash_activity

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import javax.inject.Inject

/**
 * A ViewModel for the [SplashActivity].
 */
@ActivityScope
class SplashViewModel @Inject constructor(val context: Context) : ViewModel()