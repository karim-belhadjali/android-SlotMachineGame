package com.nikoarap.slotmachine.db.classes

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
                var timestamp: Long = 0L,
                var avgSpeedInKMH: Float = 0f,
                var distanceInMeters: Int = 0,
                var timeInMillis: Long = 0L,
                var caloriesBurned: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}