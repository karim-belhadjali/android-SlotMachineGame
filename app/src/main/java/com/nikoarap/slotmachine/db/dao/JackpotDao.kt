package com.nikoarap.slotmachine.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nikoarap.slotmachine.db.classes.Configuration
import com.nikoarap.slotmachine.db.classes.User

@Dao
interface JackpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfiguration(configuration: Configuration)

    @Update
    suspend fun updateConfiguration(configuration: Configuration)

    @Query("SELECT * FROM configuration_table ")
    fun getConfiguration(): LiveData<List<Configuration>>
}