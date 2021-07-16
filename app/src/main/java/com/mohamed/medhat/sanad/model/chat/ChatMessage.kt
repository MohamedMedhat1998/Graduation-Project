package com.mohamed.medhat.sanad.model.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * A data class represents a chat message. It can represent an entity in the room database or a response
 * from the back-end.
 */
@Entity
data class ChatMessage(

    @field:SerializedName("senderName")
	@ColumnInfo(name = "senderName")
    val senderName: String,

    @field:SerializedName("dateCreated")
	@ColumnInfo(name = "dateCreated")
	val dateCreated: String,

    @field:SerializedName("mediaUri")
	@ColumnInfo(name = "mediaUri")
	val mediaUri: String,

    @field:SerializedName("id")
	@PrimaryKey
    val id: String,

    @field:SerializedName("senderUserName")
	@ColumnInfo(name = "senderUserName")
	val senderUserName: String
)
