package com.mohamed.medhat.sanad.chat

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohamed.medhat.sanad.model.chat.ChatMessage

/**
 * The data access object for the chat messages.
 */
@Dao
interface MessagesDao {
    @Query("SELECT * FROM ChatMessage")
    fun getAllMessages(): LiveData<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessages(vararg chatMessages: ChatMessage)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessages(chatMessages: List<ChatMessage>)
}