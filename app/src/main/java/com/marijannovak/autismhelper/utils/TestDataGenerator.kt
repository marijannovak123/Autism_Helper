package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.User

class TestDataGenerator {
    companion object {
        private fun userId() = "onetwothree"
        fun testUser(): User = User(userId(), "test user", "test@test.com", "lozinka123", "imgpath.jpg", emptyMap(), emptyMap())
        fun childrenList(): List<Child> = listOf( Child().apply { parentId = userId(); id = "22" })
    }
}