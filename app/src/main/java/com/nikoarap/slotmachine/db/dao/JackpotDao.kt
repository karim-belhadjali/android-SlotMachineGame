package com.nikoarap.slotmachine.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.nikoarap.slotmachine.db.classes.Configuration
import com.nikoarap.slotmachine.db.classes.User

@Dao
interface JackpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE,entity = User::class)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT COUNT(*) FROM user_table ")
     fun getUsersCount(): LiveData<Int>




}