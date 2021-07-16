package com.mohamed.medhat.sanad.ui.chat_activity.messages

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.chat.ChatMessage
import com.mohamed.medhat.sanad.ui.chat_activity.ChatActivity
import kotlinx.android.synthetic.main.item_chat_message.view.*
import kotlinx.android.synthetic.main.item_chat_message_sent.view.*

// TODO generalize
private const val VIEW_TYPE_RECEIVED_MESSAGE = 1
private const val VIEW_TYPE_SENT_MESSAGE = 2

/**
 * A [RecyclerView.Adapter] for the conversation in the [ChatActivity].
 */
class MessagesAdapter(var data: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return if (data[position].mediaUri.startsWith("https://sanad", true)) {
            VIEW_TYPE_RECEIVED_MESSAGE
        } else {
            VIEW_TYPE_SENT_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        // TODO generalize
        return if (viewType == VIEW_TYPE_RECEIVED_MESSAGE) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_message, parent, false)
            ReceivedMessagesHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_message_sent, parent, false)
            SentMessageHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SentMessageHolder) {
            holder.content.text = data[position].mediaUri
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateMessages(messages: List<ChatMessage>) {
        data = messages
        notifyDataSetChanged()
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ReceivedMessagesHolder(view: View) : RecyclerView.ViewHolder(view) {
        // TODO replace with executor
        val handler = Handler()

        init {
            itemView.setOnTouchListener { _, _ ->
                false
            }
            itemView.btn_chat_item_listen.setOnClickListener {
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(data[adapterPosition].mediaUri)
                val runnable = object : Runnable {
                    override fun run() {
                        val currentDuration = mediaPlayer.currentPosition.toLong()
                        itemView.sb_chat_item_progress.progress = currentDuration.toInt()
                        if (currentDuration.toInt() != itemView.sb_chat_item_progress.max) {
                            handler.postDelayed(this, 1)
                        }
                    }
                }
                mediaPlayer.setOnPreparedListener {
                    it.start()
                    itemView.sb_chat_item_progress.progress = 0
                    itemView.sb_chat_item_progress.max = it.duration
                    handler.postDelayed(runnable, 1)
                }
                mediaPlayer.prepareAsync()
            }
        }
    }

    inner class SentMessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = itemView.tv_sent_chat_item_content
    }
}