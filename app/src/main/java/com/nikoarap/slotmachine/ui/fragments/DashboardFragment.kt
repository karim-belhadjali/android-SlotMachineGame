package com.nikoarap.slotmachine.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.db.classes.User
import com.nikoarap.slotmachine.other.Constants.CHANGES_MADE_SUCCESSFULLY
import com.nikoarap.slotmachine.other.Constants.ENTER_ALL_VALUES
import com.nikoarap.slotmachine.other.Constants.KEY_BIG_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_EVENING_HOURS
import com.nikoarap.slotmachine.other.Constants.KEY_MORNING_HOURS
import com.nikoarap.slotmachine.other.Constants.KEY_SECOND_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_TOGGLE_FIRST_TIME
import com.nikoarap.slotmachine.ui.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject
import kotlin.math.absoluteValue


@Suppress("DEPRECATION")
@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {


    @Inject
    lateinit var sharedPreferences: SharedPreferences
    lateinit var users: List<User>

    private val CSV_HEADER="id, FirstName, LastName, Email, Telephone , BirthDate, BirthPlace"

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstConfig = sharedPreferences.getBoolean(KEY_TOGGLE_FIRST_TIME, true)
        if (!firstConfig) {

            val bigPrizeWinners = sharedPreferences.getLong(KEY_BIG_PRIZE, 0L)
            val secondPrizeWinners = sharedPreferences.getLong(KEY_SECOND_PRIZE, 0L)
            val thirdPrizeWinners = sharedPreferences.getLong(KEY_THIRD_PRIZE, 0L)
            val morningHours = sharedPreferences.getLong(KEY_MORNING_HOURS, 0L)
            val eveningHours = sharedPreferences.getLong(KEY_EVENING_HOURS, 0L)


            tv_bigPrize.setText(bigPrizeWinners.toString())
            tv_secondPrize.setText(secondPrizeWinners.toString())
            tv_thirdPrize.setText(thirdPrizeWinners.toString())
            tv_halfHours.setText(morningHours.toString())
            tv_secondHalfHours.setText(eveningHours.toString())


        }
        viewModel.userCount.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            tv_userCount.text = "Nombre de participants : ${it.absoluteValue.toString()}"
        })
        viewModel.users.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            users=it
        })

        btn_saveConfig.setOnClickListener {


            if (!firstConfig) {
                if (tv_bigPrize.text.toString() == "" || tv_secondPrize.text.toString() == "" || tv_thirdPrize.text.toString() == "" || tv_halfHours.text.toString() == "" || tv_secondHalfHours.text.toString() == "") {
                    Snackbar.make(requireView(), ENTER_ALL_VALUES, Snackbar.LENGTH_SHORT).show()
                } else {
                    val bigPrizeWinners = tv_bigPrize.text.toString().toLong()
                    val secondPrizeWinners = tv_secondPrize.text.toString().toLong()
                    val thirdPrizeWinners = tv_thirdPrize.text.toString().toLong()
                    val morningHours = tv_halfHours.text.toString().toLong()
                    val eveningHours = tv_secondHalfHours.text.toString().toLong()

                    saveConfig(
                        sharedPreferences,
                        bigPrizeWinners,
                        secondPrizeWinners,
                        thirdPrizeWinners,
                        morningHours,
                        eveningHours
                    )

                    Snackbar.make(
                        requireView(),
                        CHANGES_MADE_SUCCESSFULLY,
                        Snackbar.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(
                        R.id.action_dashboardFragment_to_registerFragment,
                        savedInstanceState
                    )


                }

            } else {

                val bigPrizeWinners = tv_bigPrize.text.toString().toLong()
                val secondPrizeWinners = tv_secondPrize.text.toString().toLong()
                val thirdPrizeWinners = tv_thirdPrize.text.toString().toLong()
                val morningHours = tv_halfHours.text.toString().toLong()
                val eveningHours = tv_secondHalfHours.text.toString().toLong()

                saveConfig(
                    sharedPreferences,
                    bigPrizeWinners,
                    secondPrizeWinners,
                    thirdPrizeWinners,
                    morningHours,
                    eveningHours
                )

                Snackbar.make(requireView(), CHANGES_MADE_SUCCESSFULLY, Snackbar.LENGTH_SHORT)
                    .show()
                sharedPreferences.edit().putBoolean(KEY_TOGGLE_FIRST_TIME, false).apply()
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_registerFragment,
                    savedInstanceState
                )

            }

        }

        btn_export.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "file/*"
            startActivityForResult(intent, 111);

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

        btn_retour.setOnClickListener {

            findNavController().navigate(
                R.id.action_dashboardFragment_to_registerFragment,
                savedInstanceState
            )
        }
    }

    private fun exportCSV() {

     /*   val fileWriter = FileWriter("customer.csv")
        try {

            fileWriter.append(CSV_HEADER)
            fileWriter.append('\n')

            for (user in users) {
                fileWriter.append(user.id.toString())
                fileWriter.append(',')
                fileWriter.append(user.firstName)
                fileWriter.append(',')
                fileWriter.append(user.lastName)
                fileWriter.append(',')
                fileWriter.append(user.email)
                fileWriter.append(',')
                fileWriter.append(user.telephone.toString())
                fileWriter.append(',')
                fileWriter.append(user.birthDate)
                fileWriter.append(',')
                fileWriter.append(user.birthPlace)
                fileWriter.append('\n')
            }

            println("Write CSV successfully!")
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
            } catch (e: IOException) {
                println("Flushing/closing error!")
                e.printStackTrace()
            }
        }*/
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