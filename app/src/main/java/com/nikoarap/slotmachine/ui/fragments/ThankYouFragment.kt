package com.nikoarap.slotmachine.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.db.classes.User
import com.nikoarap.slotmachine.other.Constants
import com.nikoarap.slotmachine.other.Constants.KEY_NAME
import com.nikoarap.slotmachine.other.Constants.KEY_NEW_USER
import com.nikoarap.slotmachine.ui.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_thank_you.*
import javax.inject.Inject
import kotlin.math.absoluteValue

@AndroidEntryPoint
class ThankYouFragment : Fragment(R.layout.fragment_thank_you) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newUser = sharedPreferences.getBoolean(Constants.KEY_NEW_USER, true)

        val userName = sharedPreferences.getString(Constants.KEY_NAME, "")

        if (userName!=""){
            tv_thankYou.setText("Thank you ${userName?.toUpperCase()} for your participation and good luck next Time")
        }
        /*if (newUser) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.thankYouFragment, true)
                .build()

            findNavController().navigate(
                R.id.action_thankYouFragment_to_dashboardFragment,
                savedInstanceState,
                navOptions
            )
        }*/

        btn_backToRegister.setOnClickListener {
            sharedPreferences.edit()
                .putBoolean(KEY_NEW_USER,true)
                .putString(KEY_NAME,"")
                .apply()
            findNavController().navigate(
                R.id.action_thankYouFragment_to_registerFragment,
                savedInstanceState

            )
        }
    }
}