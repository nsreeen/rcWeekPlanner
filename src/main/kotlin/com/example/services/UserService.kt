package com.example.services

import com.example.models.User
import com.example.models.getThisWeeksOnlineWindows

class UserService {
    fun getUser(): User {
        return User("", "", getThisWeeksOnlineWindows(),"")
    }
}