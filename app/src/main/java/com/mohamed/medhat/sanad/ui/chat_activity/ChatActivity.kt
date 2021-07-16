package com.mohamed.medhat.sanad.ui.chat_activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.chat.ChatMessage
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.ui.chat_activity.messages.MessagesAdapter
import kotlinx.android.synthetic.main.activity_chat.*
import javax.inject.Inject

/**
 * An activity for the chat screen.
 */
class ChatActivity : BaseActivity(), ChatView {

    @Inject
    lateinit var chatPresenter: ChatPresenter

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.chatViewModel())
            .get(ChatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        activityComponent.inject(this)
        chatPresenter.setView(this)
        chatPresenter.setViewModel(chatViewModel)
        chatPresenter.start(savedInstanceState)
        btn_chat_send.setOnClickListener {
            chatPresenter.onSendClicked()
        }
    }

    override fun updateChatMessages(messages: List<ChatMessage>) {
        (rv_chat_conversation.adapter as MessagesAdapter).updateMessages(messages)
        rv_chat_conversation.scrollToPosition(messages.size - 1)
    }

    override fun getMessageToSend(): String {
        return et_chat_message_to_send.text.toString()
    }

    override fun clearChatBox() {
        et_chat_message_to_send.setText("")
    }
}