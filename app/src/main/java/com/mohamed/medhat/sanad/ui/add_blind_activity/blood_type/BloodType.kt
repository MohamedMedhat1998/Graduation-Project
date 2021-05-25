package com.mohamed.medhat.sanad.ui.add_blind_activity.blood_type

import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * A class to be populated in the [BloodTypeAdapter] [ViewHolder].
 * @param name The blood type name.
 * @param isChecked Whether the blood type is selected or not.
 */
data class BloodType(val name: String, var isChecked: Boolean)