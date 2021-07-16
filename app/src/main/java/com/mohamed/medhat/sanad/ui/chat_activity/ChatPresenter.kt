package com.mohamed.medhat.sanad.ui.chat_activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.chat.ChatMessage
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.chat_activity.messages.MessagesAdapter
import com.mohamed.medhat.sanad.utils.FRAGMENT_FEATURES_BLIND_PROFILE
import com.mohamed.medhat.sanad.utils.PREFS_USER_EMAIL
import com.mohamed.medhat.sanad.utils.PREFS_USER_FIRST_NAME
import kotlinx.android.synthetic.main.activity_chat.*
import javax.inject.Inject

/**
 * An mvp presenter for [ChatActivity].
 */
@ActivityScope
class ChatPresenter @Inject constructor(val sharedPrefs: SharedPrefs) :
    AdvancedPresenter<ChatView, ChatViewModel>() {

    private lateinit var chatView: ChatView
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatActivity: ChatActivity
    private lateinit var blindMiniProfile: BlindMiniProfile
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var rvChatConversation: RecyclerView

    override fun start(savedInstanceState: Bundle?) {
        initializeConversationRecyclerView()
        chatView.displayToast("Chatting with ${blindMiniProfile.firstName} ${blindMiniProfile.lastName}")
        chatViewModel.chatMessages.observe(chatActivity) {
            Log.d("MessagesPresenter", "${it.size}")
            chatView.updateChatMessages(it)
        }
        chatViewModel.isMessageSent.observe(chatActivity) {
            // TODO update this observer
        }
    }

    override fun setView(view: ChatView) {
        chatView = view
        chatActivity = view as ChatActivity
        val extras = chatActivity.intent.extras
        blindMiniProfile =
            extras!!.getSerializable(FRAGMENT_FEATURES_BLIND_PROFILE) as BlindMiniProfile
        rvChatConversation = chatActivity.rv_chat_conversation
    }

    override fun setViewModel(viewModel: ChatViewModel) {
        chatViewModel = viewModel
    }

    private fun initializeConversationRecyclerView() {
        messagesAdapter = MessagesAdapter(mutableListOf())
        rvChatConversation.layoutManager = LinearLayoutManager(chatActivity)
        rvChatConversation.adapter = messagesAdapter
    }

    fun onSendClicked() {
        if (chatView.getMessageToSend().isEmpty()) {
            chatView.displayToast(chatActivity.getString(R.string.please_type_a_message))
            return
        }
        val chatMessage = ChatMessage(
            sharedPrefs.read(PREFS_USER_FIRST_NAME),
            System.currentTimeMillis().toString(),
            chatView.getMessageToSend(),
            System.currentTimeMillis().toString(),
            sharedPrefs.read(
                PREFS_USER_EMAIL
            )
        )
        chatView.clearChatBox()
        chatViewModel.sendMessage(chatMessage, blindMiniProfile.userName)
    }
}