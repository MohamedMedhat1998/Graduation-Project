package com.mohamed.medhat.sanad.ui.main_activity.blinds

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.BlindAddProfile
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.utils.VIEW_TYPE_MAIN_ADD_PROFILE
import com.mohamed.medhat.sanad.utils.VIEW_TYPE_MAIN_BLIND_PROFILE
import kotlinx.android.synthetic.main.item_blind_holder.view.*

/**
 * A [RecyclerView.Adapter] for the blinds list in the [MainActivity].
 */
class BlindsAdapter(
    private var blinds: MutableList<BlindItem>,
    val onAddBlindClicked: () -> Unit,
    val onProfileClicked: (profile: BlindMiniProfile) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return when (blinds[position]) {
            is BlindAddProfile -> VIEW_TYPE_MAIN_ADD_PROFILE
            else -> VIEW_TYPE_MAIN_BLIND_PROFILE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            VIEW_TYPE_MAIN_ADD_PROFILE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_add_blind_holder, parent, false)
                AddBlindViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_blind_holder, parent, false)
                BlindViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BlindViewHolder -> {
                val data = (blinds[position] as BlindMiniProfile)
                holder.itemView.tv_item_blind_name.text = context.getString(
                    R.string.blind_profile_name_holder,
                    data.firstName,
                    data.lastName
                )
                Glide.with(context)
                    .load(data.profilePicture)
                    .circleCrop()
                    .placeholder(R.drawable.on_boarding_tab_normal)
                    .into(holder.itemView.iv_item_blind_profile_pic)
            }
            is AddBlindViewHolder -> {
                // TODO decide whether to bind something or to keep it a static view.
            }
        }
    }

    override fun getItemCount(): Int {
        return blinds.size
    }

    /**
     * Updates the list of the blinds with the new passed list.
     * @param newBlinds The updated items list that should be used.
     */
    fun updateBlindsList(newBlinds: List<BlindItem>) {
        blinds = newBlinds as MutableList<BlindItem>
        notifyDataSetChanged()
    }

    inner class BlindViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onProfileClicked.invoke((blinds[adapterPosition] as BlindMiniProfile))
            }
        }
    }

    inner class AddBlindViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onAddBlindClicked.invoke()
            }
        }
    }
}