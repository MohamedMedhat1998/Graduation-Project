package com.mohamed.medhat.sanad.ui.persons_manager_activity.persons

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.KnownPerson
import com.mohamed.medhat.sanad.model.gps_errors.KnownPersonAdd
import com.mohamed.medhat.sanad.utils.VIEW_TYPE_KNOWN_PERSON
import com.mohamed.medhat.sanad.utils.VIEW_TYPE_KNOWN_PERSON_ADD
import kotlinx.android.synthetic.main.item_person_holder.view.*

/**
 * A [RecyclerView.Adapter] that is responsible for populating the [KnownPerson]s list.
 */
class PersonsAdapter(
    private var persons: List<PersonItem> = listOf(),
    private val onAddPersonClicked: () -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return when (persons[position]) {
            is KnownPerson -> VIEW_TYPE_KNOWN_PERSON
            is KnownPersonAdd -> VIEW_TYPE_KNOWN_PERSON_ADD
            else -> VIEW_TYPE_KNOWN_PERSON
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        when (viewType) {
            VIEW_TYPE_KNOWN_PERSON -> {
                val view = inflater.inflate(R.layout.item_person_holder, parent, false)
                return KnownPersonHolder(view)
            }
            VIEW_TYPE_KNOWN_PERSON_ADD -> {
                val view = inflater.inflate(R.layout.item_add_person_holder, parent, false)
                return AddPersonHolder(view)
            }
        }
        val view = inflater.inflate(R.layout.item_person_holder, parent, false)
        return KnownPersonHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KnownPersonHolder -> {
                val person = persons[position] as KnownPerson
                holder.itemView.tv_item_person_name.text = person.name
                if (person.personPictures.isNotEmpty()) {
                    Glide.with(context)
                        .load(person.personPictures[0].uri)
                        .circleCrop()
                        .placeholder(R.drawable.on_boarding_tab_normal)
                        .into(holder.itemView.iv_item_person_pic)
                } else {
                    Glide.with(context)
                        .load(R.drawable.on_boarding_tab_normal)
                        .circleCrop()
                        .placeholder(R.drawable.on_boarding_tab_normal)
                        .into(holder.itemView.iv_item_person_pic)
                }
            }
            is AddPersonHolder -> {
                // TODO decide whether to do something or to keep it a static view.
            }
        }
    }

    override fun getItemCount(): Int {
        return persons.size
    }

    /**
     * Refreshes the recycler view with the new data.
     * @param newPersons The new data to be used inside the recycler view.
     */
    fun update(newPersons: List<PersonItem>) {
        persons = newPersons
        notifyDataSetChanged()
    }

    inner class KnownPersonHolder(view: View) : RecyclerView.ViewHolder(view) {
        // TODO handle on click
    }

    inner class AddPersonHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onAddPersonClicked.invoke()
            }
        }
    }
}