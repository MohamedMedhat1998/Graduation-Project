package com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses

import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * A class to be populated in the [IllnessesAdapter] [ViewHolder].
 * @param name The illness name.
 * @param isChecked Whether or not the blind suffers from this illness.
 */
data class IllnessItem(val name: String, var isChecked: Boolean)