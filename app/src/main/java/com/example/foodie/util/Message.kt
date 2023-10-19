package com.example.foodie.util

import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.util.Date

class Message(val messageId:String, val messageText:String, var messageUser:IUser, val messageDate:Date, var messageUrl:String) : IMessage, MessageContentType.Image {
    override fun getId(): String {
        return messageId
    }

    override fun getText(): String {
        return messageText
    }

    override fun getUser(): IUser {
        return messageUser
    }

    override fun getCreatedAt(): Date {
        return messageDate
    }

    override fun getImageUrl(): String? {
        if(messageUrl.equals("")){
            return null
        }
        return messageUrl
    }
}