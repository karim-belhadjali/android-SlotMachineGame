package com.nikoarap.slotmachine.db.classes

import android.graphics.Bitmap
import android.provider.ContactsContract
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user_table")
data class User(
    var firstName: String,
    var lastName: String,
    var email: String,
    var telephone: String,
    var birthDate: String,
    var birthPlace: String,
    var ville: String,
    var codePostale: String,
    var residencePlace: String,
    var acceptedPlans:Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}