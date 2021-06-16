package com.nikoarap.slotmachine.db.classes

import android.graphics.Bitmap
import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "configuration_table")
data class Configuration(

    var bigPrizeWinners : Long,

    var secondPrizeWinners: Long ,

    var thirdPrizeWinners: Long,

    var firstHalfHours : Long,

    var secondHalfHours: Long

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}