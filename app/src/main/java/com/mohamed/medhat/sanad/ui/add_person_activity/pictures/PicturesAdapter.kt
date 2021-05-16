package com.mohamed.medhat.sanad.ui.add_person_activity.pictures

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamed.medhat.sanad.R
import kotlinx.android.synthetic.main.item_picture_holder.view.*

/**
 * A [RecyclerView.Adapter] that displays a list of pictures in a [RecyclerView].
 */
class PicturesAdapter(
    private val pictures: MutableList<Uri>,
    private val onDeletePictureClicked: (picture: Uri) -> Unit,
    private val onPictureClicked: (picture: Uri) -> Unit
) :
    RecyclerView.Adapter<PicturesAdapter.PictureHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
        context = parent.context
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_picture_holder, parent, false)
        return PictureHolder(view)
    }

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        Glide.with(context).load(pictures[position]).into(holder.itemView.iv_item_picture_picture)
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    /**
     * Updates the pictures inside the recycler view.
     */
    fun updatePictures(list: List<Uri>) {
        pictures.clear()
        pictures.addAll(list)
        notifyDataSetChanged()
    }

    inner class PictureHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.btn_item_picture_delete.setOnClickListener {
                onDeletePictureClicked.invoke(pictures[adapterPosition])
                pictures.removeAt(adapterPosition)
                notifyDataSetChanged()
            }
            itemView.iv_item_picture_picture.setOnClickListener {
                onPictureClicked.invoke(pictures[adapterPosition])
            }
        }
    }
}