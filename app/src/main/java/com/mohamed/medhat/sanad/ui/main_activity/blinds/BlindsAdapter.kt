package com.mohamed.medhat.sanad.ui.main_activity.blinds

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import kotlinx.android.synthetic.main.item_blind_holder.view.*

/**
 * A [RecyclerView.Adapter] for the blinds list in the [MainActivity].
 */
class BlindsAdapter(private var blinds: MutableList<BlindMiniProfile>) :
    RecyclerView.Adapter<BlindsAdapter.BlindViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlindViewHolder {
        // TODO add the "ADD_NEW_BLIND" view type.
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_blind_holder, parent, false)
        return BlindViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlindViewHolder, position: Int) {
        holder.itemView.tv_item_blind_name.text = context.getString(
            R.string.blind_profile_name_holder,
            blinds[position].firstName,
            blinds[position].lastName
        )
        Glide.with(context)
            .load(blinds[position].profilePicture)
            .circleCrop()
            .placeholder(R.drawable.on_boarding_tab_normal)
            .into(holder.itemView.iv_item_blind_profile_pic)
    }

    override fun getItemCount(): Int {
        return blinds.size
    }

    /**
     * Updates the list of the blinds with the new passed list.
     * @param newBlinds The updated items list that should be used.
     */
    fun updateBlindsList(newBlinds: List<BlindMiniProfile>) {
        blinds = newBlinds as MutableList<BlindMiniProfile>
        notifyDataSetChanged()
    }

    inner class BlindViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                // TODO find a way to display the FragmentBottomSheet that contains the blind details.
                // TODO distinguish between the normal item click and the add blind click
            }
        }
    }
}