package com.nikoarap.slotmachine.repositories

import com.nikoarap.slotmachine.db.classes.Configuration
import com.nikoarap.slotmachine.db.classes.User
import com.nikoarap.slotmachine.db.dao.JackpotDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    val jackpotDao: JackpotDao
) {

    suspend fun insertUser(user: User) = jackpotDao.insertUser(user)

    suspend fun deleteUser(user: User) = jackpotDao.deleteUser(user)

    suspend fun updateUser(user: User) = jackpotDao.updateUser(user)

    suspend fun insertConfiguration(configuration: Configuration) =
        jackpotDao.insertConfiguration(configuration)

    suspend fun updateConfiguration(configuration: Configuration) =
        jackpotDao.updateConfiguration(configuration)

     fun getConfiguration() =
        jackpotDao.getConfiguration()
}