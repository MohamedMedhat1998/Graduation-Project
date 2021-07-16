package com.mohamed.medhat.sanad.ui.chat_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.sanad.chat.MessagesDao
import com.mohamed.medhat.sanad.model.chat.ChatMessage
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * A ViewModel for the [ChatActivity].
 */
class ChatViewModel @Inject constructor(val messagesDao: MessagesDao, val webApi: WebApi) :
    BaseViewModel() {

    private val _chatMessages = messagesDao.getAllMessages()
    val chatMessages = Transformations.switchMap(_chatMessages) {
        messagesDao.getAllMessages()
    }

    private val _isMessageSent = MutableLiveData<Boolean>()
    val isMessageSent: LiveData<Boolean>
        get() = _isMessageSent

    /**
     * Sends a chat message to the server.
     * @param chatMessage The message to send.
     */
    fun sendMessage(chatMessage: ChatMessage, receiverUsername: String) {
        viewModelScope.launch {
            val receiverPart =
                MultipartBody.Part.createFormData("ReceiverUserName", receiverUsername)
            val messagePart = MultipartBody.Part.createFormData("Body", chatMessage.mediaUri)
            val chatResponse = webApi.sendChatMessage(receiverPart, messagePart)
            if (chatResponse.isSuccessful) {
                _isMessageSent.postValue(true)
                withContext(Dispatchers.IO) {
                    messagesDao.insertMessages(chatMessage)
                }
                _isMessageSent.postValue(false)
            } else {
                _isMessageSent.postValue(false)
                Log.e("ChatError", "${chatResponse.code()}:${chatResponse.message()}")
            }
        }
    }
}