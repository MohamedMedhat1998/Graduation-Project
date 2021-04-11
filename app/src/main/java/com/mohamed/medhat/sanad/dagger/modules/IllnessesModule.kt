package com.mohamed.medhat.sanad.dagger.modules

import android.content.Context
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.annotations.Illnesses
import com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses.IllnessItem
import dagger.Module
import dagger.Provides

/**
 * Provides illnesses-adapter and its related objects.
 */
@Module
class IllnessesModule {

    @Provides
    @Illnesses
    fun provideIllnessesItems(context: Context): MutableList<IllnessItem> {
        val items = mutableListOf<IllnessItem>()
        context.resources.getStringArray(R.array.illnesses).forEach {
            items.add(IllnessItem(it, false))
        }
        return items
    }
}