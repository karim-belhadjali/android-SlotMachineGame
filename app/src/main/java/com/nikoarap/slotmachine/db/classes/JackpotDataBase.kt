package com.nikoarap.slotmachine.db.classes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikoarap.slotmachine.db.dao.JackpotDao

@Database(
    entities = [User::class],
    version = 2
)
abstract class JackpotDataBase : RoomDatabase() {

    abstract fun getJackpotDao(): JackpotDao
}