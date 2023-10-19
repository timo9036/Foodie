package com.example.foodie.util

import com.stfalcon.chatkit.commons.models.IUser

class User(val userId: String, val userName: String, val userAvatar: String) : IUser {
    override fun getId(): String {
        return userId
    }

    override fun getName(): String {
        return userName
    }

    override fun getAvatar(): String {
        return userAvatar
    }
}