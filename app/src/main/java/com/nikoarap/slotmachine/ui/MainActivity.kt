package com.nikoarap.slotmachine.ui

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.other.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


        sharedPreferences.edit()
            .putLong(Constants.KEY_SECOND_PRIZE_THIRD, 0L)
            .apply()
    }

    override fun onBackPressed() {
    }


}
