package com.mohamed.medhat.sanad.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import com.mohamed.medhat.sanad.utils.SHARED_PREFERENCES_FILE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * A dagger module for providing SharedPreferences-related objects.
 */
@Module
class SharedPreferencesModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }
}