package com.mohamed.medhat.sanad.ui.chat_activity

import com.mohamed.medhat.sanad.model.chat.ChatMessage
import com.mohamed.medhat.sanad.ui.base.BaseView

/**
 * An mvp view for [ChatActivity].
 */
interface ChatView: BaseView {
    fun updateChatMessages(messages: List<ChatMessage>)
    fun getMessageToSend(): String
    fun clearChatBox()
}