package com.mohamed.medhat.sanad.chat

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohamed.medhat.sanad.model.chat.ChatMessage

/**
 * The room database for the chat messages.
 */
@Database(entities = [ChatMessage::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
}