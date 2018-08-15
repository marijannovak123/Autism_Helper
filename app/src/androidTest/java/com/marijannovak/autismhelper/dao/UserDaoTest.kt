package com.marijannovak.autismhelper.dao

import com.marijannovak.autismhelper.data.database.dao.UserDao
import com.marijannovak.autismhelper.util.BaseDaoTest
import com.marijannovak.autismhelper.utils.TestDataGenerator
import org.junit.Before
import org.junit.Test

class UserDaoTest: BaseDaoTest() {

    private val testUser = TestDataGenerator.testUser()
    private val testChildren = TestDataGenerator.childrenList()
    private var userId = -1

    private lateinit var userDao: UserDao

    @Before
    override fun setUp() {
        super.setUp()
        userDao = db.userDao()
        userDao.getCurrentUser().test().assertNoValues()

        userId = userDao
                .insert(testUser)
                .toInt()

        db.childDao().insertMultiple(testChildren)
    }

    @Test
    fun inserted() {
        userDao.getCurrentUser()
                .test()
                .assertValue(testUser)
    }

    @Test
    fun update() {
        val newUsername = "new_username"
        val newParentPassword = "new_password"
        userDao.update(newUsername, newParentPassword)

        userDao.getCurrentUser()
                .test()
                .assertValue { it.username == newUsername && it.parentPassword == newParentPassword }
    }

    @Test
    fun updateWithProfilePic() {
        val newUsername = "new_username"
        val newParentPassword = "new_password"
        val newProfilePic = "new_profile_pic"
        userDao.updateAll(newUsername, newParentPassword, newProfilePic)

        userDao.getCurrentUser()
                .test()
                .assertValue { it.username == newUsername && it.parentPassword == newParentPassword && it.profilePicPath == newProfilePic }
    }

    @Test
    fun delete() {
        userDao.delete(testUser)
        userDao.getAllUsers()
                .test()
                .assertValue { it.isEmpty() }
    }

    @Test
    fun deleteAll() {
        userDao.deleteTable()
        userDao.getAllUsers()
                .test()
                .assertValue { it.isEmpty() }
    }

    @Test
    fun getWithChildren() {
        userDao.getUserWithChildren()
                .test()
                .assertValue { it.children == testChildren }
                .assertValue { it.user == testUser }
    }

    @Test
    fun notLoggedInWithEmptyDb() {
        userDao.deleteTable()

        userDao.userLoggedIn()
                .test()
                .assertNoValues()
    }

    @Test
    fun loggedIn() {
        userDao.userLoggedIn()
                .test()
                .assertValue(testUser)
    }
}