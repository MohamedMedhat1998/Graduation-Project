package com.mohamed.medhat.sanad.ui.add_blind_activity.blood_type

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.add_blind_activity.AddBlindActivity
import kotlinx.android.synthetic.main.item_blood_type.view.*

/**
 * An adapter for handling the "BloodTypeRecyclerView" in the [AddBlindActivity].
 */
class BloodTypeAdapter(val data: List<BloodType>) :
    RecyclerView.Adapter<BloodTypeAdapter.BloodTypeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodTypeHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_blood_type, parent, false)
        return BloodTypeHolder(view)
    }

    override fun onBindViewHolder(holder: BloodTypeHolder, position: Int) {
        holder.itemView.rb_item_blood_type.isChecked = data[position].isChecked
        holder.itemView.rb_item_blood_type.text = data[position].name
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @return The selected item from the list.
     */
    fun getCheckedItem(): BloodType {
        data.forEach {
            if (it.isChecked) {
                return it
            }
        }
        return data[0]
    }

    inner class BloodTypeHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.rb_item_blood_type.setOnClickListener {
                data.forEachIndexed { index, bloodType ->
                    bloodType.isChecked = index == adapterPosition
                }
                notifyDataSetChanged()
            }
        }
    }
}