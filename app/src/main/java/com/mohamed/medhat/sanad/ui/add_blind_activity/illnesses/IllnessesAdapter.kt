package com.mohamed.medhat.sanad.ui.add_blind_activity.illnesses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.annotations.Illnesses
import com.mohamed.medhat.sanad.ui.add_blind_activity.AddBlindActivity
import kotlinx.android.synthetic.main.item_illness_holder.view.*
import javax.inject.Inject

/**
 * An adapter for handling the "IllnessesRecyclerView" in the [AddBlindActivity].
 */
class IllnessesAdapter @Inject constructor(@Illnesses val illnessItems: MutableList<IllnessItem>) :
    RecyclerView.Adapter<IllnessesAdapter.IllnessHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IllnessHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_illness_holder, parent, false)
        return IllnessHolder(view)
    }

    override fun onBindViewHolder(holder: IllnessHolder, position: Int) {
        holder.itemView.cb_item_illness.apply {
            text = illnessItems[position].name
            isChecked = illnessItems[position].isChecked
        }
    }

    override fun getItemCount(): Int {
        return illnessItems.size
    }

    inner class IllnessHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                illnessItems[adapterPosition].isChecked = !illnessItems[adapterPosition].isChecked
                notifyItemChanged(adapterPosition)
            }
        }
    }

    /**
     * Adds an [IllnessItem] to the list so that the mentor can check.
     * This function adds the item at the end and automatically notifies the dataset change.
     * @param illnessItem The item to add.
     */
    fun addIllness(illnessItem: IllnessItem) {
        illnessItems.add(illnessItem)
        notifyDataSetChanged()
    }

    /**
     * @return A list of the names of the checked illness items.
     */
    fun getCheckedIllnesses(): List<String> {
        val names = mutableListOf<String>()
        illnessItems.forEach {
            names.add(it.name)
        }
        return names
    }
}