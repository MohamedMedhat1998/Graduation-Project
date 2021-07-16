package com.mohamed.medhat.sanad.dagger.modules

import android.content.Context
import androidx.room.Room
import com.mohamed.medhat.sanad.chat.ChatDatabase
import com.mohamed.medhat.sanad.chat.MessagesDao
import com.mohamed.medhat.sanad.utils.CHAT_DATABASE_FILENAME
import com.mohamed.medhat.sanad.utils.CHAT_HUB_URL
import com.mohamed.medhat.sanad.utils.managers.TokenManager
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnection
import com.smartarmenia.dotnetcoresignalrclientjava.WebSocketHubConnectionP2
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Responsible for providing chat-related objects.
 * Usually, objects provided by this module are singletons.
 */
@Module
class ChatModule {

    @Singleton
    @Provides
    fun provideHubConnection(tokenManager: TokenManager): HubConnection {
        val connection = WebSocketHubConnectionP2(CHAT_HUB_URL, "Bearer ${tokenManager.getToken()}")
        return connection
    }

    @Singleton
    @Provides
    fun provideChatDatabase(applicationContext: Context): ChatDatabase {
        return Room.databaseBuilder(
            applicationContext,
            ChatDatabase::class.java,
            CHAT_DATABASE_FILENAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideMessagesDao(chatDatabase: ChatDatabase): MessagesDao {
        return chatDatabase.messagesDao()
    }
}


