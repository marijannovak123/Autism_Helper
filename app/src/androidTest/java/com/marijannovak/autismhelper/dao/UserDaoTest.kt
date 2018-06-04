package com.marijannovak.autismhelper.dao

import com.marijannovak.autismhelper.util.BaseDaoTest
import com.marijannovak.autismhelper.utils.TestDataGenerator
import org.junit.Test

class UserDaoTest: BaseDaoTest() {

    val user = TestDataGenerator.testUser()


    @Test
    fun insert() {
        db.userDao().insert(user)

        db.userDao()
                .getCurrentUser()
                .test()
                .assertValue {
                    it.id == user.id &&
                            it.email == user.email &&
                            it.username == it.username &&
                            it.profilePicPath == user.profilePicPath
                            && it.parentPassword == user.parentPassword
                }
        //todo: override User's equals method
    }

    @Test
    fun update() {

    }
}