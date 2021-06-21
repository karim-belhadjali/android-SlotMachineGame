package com.nikoarap.slotmachine.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.nikoarap.slotmachine.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE



    }

    override fun onBackPressed() {
    }


}
