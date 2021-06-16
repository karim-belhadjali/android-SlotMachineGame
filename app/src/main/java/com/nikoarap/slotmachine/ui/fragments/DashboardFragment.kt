package com.nikoarap.slotmachine.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.db.classes.Configuration
import com.nikoarap.slotmachine.other.Constants.CHANGES_MADE_SUCCESSFULLY
import com.nikoarap.slotmachine.other.Constants.ENTER_ALL_VALUES
import com.nikoarap.slotmachine.other.Constants.KEY_BIG_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_EVENING_HOURS
import com.nikoarap.slotmachine.other.Constants.KEY_MORNING_HOURS
import com.nikoarap.slotmachine.other.Constants.KEY_SECOND_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_TOGGLE_FIRST_TIME
import com.nikoarap.slotmachine.ui.viewmodels.ConfigurationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val viewModel: ConfigurationViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstConfig = sharedPreferences.getBoolean(KEY_TOGGLE_FIRST_TIME, true)
        if (!firstConfig) {

            var bigPrizeWinners = sharedPreferences.getLong(KEY_BIG_PRIZE, 0L)
            var secondPrizeWinners = sharedPreferences.getLong(KEY_SECOND_PRIZE, 0L)
            var thirdPrizeWinners = sharedPreferences.getLong(KEY_THIRD_PRIZE, 0L)
            var morningHours = sharedPreferences.getLong(KEY_MORNING_HOURS, 0L)
            var eveningHours = sharedPreferences.getLong(KEY_EVENING_HOURS, 0L)


            tv_bigPrize.setText(bigPrizeWinners.toString())
            tv_secondPrize.setText(secondPrizeWinners.toString())
            tv_thirdPrize.setText(thirdPrizeWinners.toString())
            tv_halfHours.setText(morningHours.toString())
            tv_secondHalfHours.setText(eveningHours.toString())


        }

        btn_saveConfig.setOnClickListener {


            if (!firstConfig) {
                if (tv_bigPrize.text.isNullOrEmpty() || tv_secondPrize.text.isNullOrEmpty() || tv_thirdPrize.text.isNullOrEmpty() || tv_halfHours.text.isNullOrEmpty() || tv_secondHalfHours.text.isNullOrEmpty()) {
                    Snackbar.make(requireView(), ENTER_ALL_VALUES, Snackbar.LENGTH_SHORT).show()
                } else {
                    val bigPrizeWinners = tv_bigPrize.text.toString().toLong()
                    val secondPrizeWinners = tv_secondPrize.text.toString().toLong()
                    val thirdPrizeWinners = tv_thirdPrize.text.toString().toLong()
                    val morningHours = tv_halfHours.text.toString().toLong()
                    val eveningHours = tv_secondHalfHours.text.toString().toLong()

                    saveConfig(sharedPreferences,bigPrizeWinners,secondPrizeWinners,thirdPrizeWinners,morningHours,eveningHours)

                    Snackbar.make(
                        requireView(),
                        CHANGES_MADE_SUCCESSFULLY,
                        Snackbar.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(
                        R.id.action_dashboardFragment_to_jackpotFragment,
                        savedInstanceState
                    )


                }

            } else {

                val bigPrizeWinners = tv_bigPrize.text.toString().toLong()
                val secondPrizeWinners = tv_secondPrize.text.toString().toLong()
                val thirdPrizeWinners = tv_thirdPrize.text.toString().toLong()
                val morningHours = tv_halfHours.text.toString().toLong()
                val eveningHours = tv_secondHalfHours.text.toString().toLong()

                saveConfig(sharedPreferences,bigPrizeWinners,secondPrizeWinners,thirdPrizeWinners,morningHours,eveningHours)

                Snackbar.make(requireView(), CHANGES_MADE_SUCCESSFULLY, Snackbar.LENGTH_SHORT)
                    .show()
                sharedPreferences.edit().putBoolean(KEY_TOGGLE_FIRST_TIME, false).apply()
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_jackpotFragment,
                    savedInstanceState
                )

            }
        }

    }

    private fun saveConfig(
        sharedPreferences: SharedPreferences,
        bigPrizeWinners: Long,
        secondPrizeWinners: Long,
        thirdPrizeWinners: Long,
        morningHours: Long,
        eveningHours: Long
    ) {
        sharedPreferences.edit()
            .putLong(KEY_BIG_PRIZE, bigPrizeWinners)
            .putLong(KEY_SECOND_PRIZE, secondPrizeWinners)
            .putLong(KEY_THIRD_PRIZE, thirdPrizeWinners)
            .putLong(KEY_MORNING_HOURS, morningHours)
            .putLong(KEY_EVENING_HOURS, eveningHours)
            .apply()

    }


}