package com.marijannovak.autismhelper.util

import com.marijannovak.autismhelper.data.models.User

class TestDataGenerator {
    companion object {
        fun testUser(): User = User("onetwothree", "test user", "test@test.com", "lozinka123", "imgpath.jpg", emptyMap(), emptyMap())
    }
}