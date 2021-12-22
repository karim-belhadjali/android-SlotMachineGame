package com.nikoarap.slotmachine.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat
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
import com.nikoarap.slotmachine.other.Constants.KEY_SECOND_PRIZE_FIRST
import com.nikoarap.slotmachine.other.Constants.KEY_SECOND_PRIZE_SECOND
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE_FIRST
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE_FOURTH
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE_SECOND
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE_THIRD
import com.nikoarap.slotmachine.other.Constants.KEY_TOGGLE_FIRST_TIME
import com.nikoarap.slotmachine.other.Constants.KEY_WIN_AFTER
import com.nikoarap.slotmachine.ui.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import pub.devrel.easypermissions.EasyPermissions.hasPermissions
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.math.absoluteValue


@Suppress("DEPRECATION")
@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {


    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var users: List<User>


    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstConfig = sharedPreferences.getBoolean(KEY_TOGGLE_FIRST_TIME, true)
        if (!firstConfig) {

            val bigPrizeWinners = sharedPreferences.getLong(KEY_BIG_PRIZE, 0L)
            val secondPrizeWinners = sharedPreferences.getLong(KEY_SECOND_PRIZE, 0L)
            val secondPrizeFirstWinners = sharedPreferences.getLong(KEY_SECOND_PRIZE_FIRST, 0L)
            val secondPrizeSecondWinners = sharedPreferences.getLong(KEY_SECOND_PRIZE_SECOND, 0L)
            val playerswinAfterr = sharedPreferences.getLong(KEY_WIN_AFTER, 0L)
            val thirdPrizeWinners = sharedPreferences.getLong(KEY_THIRD_PRIZE, 0L)
            val thirdPrizeFirstWinners = sharedPreferences.getLong(KEY_THIRD_PRIZE_FIRST, 0L)
            val thirdPrizeSecondWinners = sharedPreferences.getLong(KEY_THIRD_PRIZE_SECOND, 0L)
            val thirdPrizeThirdWinners = sharedPreferences.getLong(KEY_THIRD_PRIZE_THIRD, 0L)
            //val thirdPrizeFourthWinners = sharedPreferences.getLong(KEY_THIRD_PRIZE_FOURTH, 0L)
            val morningHours = sharedPreferences.getLong(KEY_MORNING_HOURS, 0L)
            val eveningHours = sharedPreferences.getLong(KEY_EVENING_HOURS, 0L)


            tv_bigPrize.setText(bigPrizeWinners.toString())
            tv_playersWinAfter.setText(playerswinAfterr.toString())
            tv_secondPrize_one.setText(secondPrizeFirstWinners.toString())
            tv_secondPrize_two.setText(secondPrizeSecondWinners.toString())
            tv_thirdPrize_one.setText(thirdPrizeFirstWinners.toString())
            tv_thirdPrize_two.setText(thirdPrizeSecondWinners.toString())
            tv_thirdPrize_three.setText(thirdPrizeThirdWinners.toString())
            //tv_thirdPrize_four.setText(thirdPrizeFourthWinners.toString())
            tv_halfHours.setText(morningHours.toString())
            tv_secondHalfHours.setText(eveningHours.toString())


        }
        viewModel.userCount.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            tv_userCount.text = "Nombre de participants : ${it.absoluteValue.toString()}"
        })
        viewModel.users.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            users = it
        })

        btn_saveConfig.setOnClickListener {


            if (!firstConfig) {
                if (tv_bigPrize.text.toString() == "" || tv_secondPrize_one.text.toString() == "" || tv_secondPrize_two.text.toString() == "" || tv_playersWinAfter.text.toString() == "" ||
                    tv_thirdPrize_one.text.toString() == "" || tv_thirdPrize_two.text.toString() == "" || tv_thirdPrize_three.text.toString() == "" ||// tv_thirdPrize_four.text.toString() == "" ||
                    tv_halfHours.text.toString() == "" || tv_secondHalfHours.text.toString() == ""
                ) {
                    Snackbar.make(requireView(), ENTER_ALL_VALUES, Snackbar.LENGTH_SHORT).show()
                } else {
                    val bigPrizeWinners = tv_bigPrize.text.toString().toLong()
                    val secondPrizeFirstWinners = tv_secondPrize_one.text.toString().toLong()
                    val secondPrizeSecondWinners = tv_secondPrize_two.text.toString().toLong()
                    val thirdPrizeFirstWinners = tv_thirdPrize_one.text.toString().toLong()
                    val thirdPrizeSecondWinners = tv_thirdPrize_two.text.toString().toLong()
                    val thirdPrizeThirdWinners = tv_thirdPrize_three.text.toString().toLong()
                    //val thirdPrizeFourthWinners = tv_thirdPrize_four.text.toString().toLong()
                    val thirdPrizeFourthWinners = 0L
                    val playerswinAfter = tv_playersWinAfter.text.toString().toLong()
                    val morningHours = tv_halfHours.text.toString().toLong()
                    val eveningHours = tv_secondHalfHours.text.toString().toLong()

                    saveConfig(
                        sharedPreferences,
                        bigPrizeWinners,
                        secondPrizeFirstWinners,
                        secondPrizeSecondWinners,
                        playerswinAfter,
                        thirdPrizeFirstWinners,
                        thirdPrizeSecondWinners,
                        thirdPrizeThirdWinners,
                        thirdPrizeFourthWinners,
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
                if (tv_bigPrize.text.toString() == "" || tv_secondPrize_one.text.toString() == "" || tv_secondPrize_two.text.toString() == "" || tv_playersWinAfter.text.toString() == "" ||
                    tv_thirdPrize_one.text.toString() == "" || tv_thirdPrize_two.text.toString() == "" || tv_thirdPrize_three.text.toString() == "" || //tv_thirdPrize_four.text.toString() == "" ||
                    tv_halfHours.text.toString() == "" || tv_secondHalfHours.text.toString() == ""
                ) {
                    Snackbar.make(requireView(), ENTER_ALL_VALUES, Snackbar.LENGTH_SHORT).show()
                } else {
                    val bigPrizeWinners = tv_bigPrize.text.toString().toLong()
                    val secondPrizeFirstWinners = tv_secondPrize_one.text.toString().toLong()
                    val secondPrizeSecondWinners = tv_secondPrize_two.text.toString().toLong()
                    val playerswinAfter = tv_playersWinAfter.text.toString().toLong()
                    val thirdPrizeFirstWinners = tv_thirdPrize_one.text.toString().toLong()
                    val thirdPrizeSecondWinners = tv_thirdPrize_two.text.toString().toLong()
                    val thirdPrizeThirdWinners = tv_thirdPrize_three.text.toString().toLong()
                    //val thirdPrizeFourthWinners = tv_thirdPrize_four.text.toString().toLong()
                    val thirdPrizeFourthWinners = 0L
                    val morningHours = tv_halfHours.text.toString().toLong()
                    val eveningHours = tv_secondHalfHours.text.toString().toLong()

                    saveConfig(
                        sharedPreferences,
                        bigPrizeWinners,
                        secondPrizeFirstWinners,
                        secondPrizeSecondWinners,
                        playerswinAfter,
                        thirdPrizeFirstWinners,
                        thirdPrizeSecondWinners,
                        thirdPrizeThirdWinners,
                        thirdPrizeFourthWinners,
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

        }

        btn_export.setOnClickListener {
            createExcel(users)
        }

        btn_retour.setOnClickListener {

            findNavController().navigate(
                R.id.action_dashboardFragment_to_registerFragment,
                savedInstanceState
            )
        }
    }


    fun createExcel(users: List<User>) {
        if (!hasPermissions(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            requestWritePermissions()
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                exportExcel(users)

               findNavController().navigate(
                    R.id.action_dashboardFragment_to_registerFragment
                )
            }

        }


    }


    suspend fun exportExcel(users: List<User>) {
        var filePath: File? = null

        filePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/Users.xls"
            )
        } else {
            File(Environment.getExternalStorageDirectory().toString() +"/Users.xls")
        }
        //val filePath = File("/storage/emulated/0/Download" + "/Users.xls")
        val hssfWorkbook = HSSFWorkbook()
        val hssfSheet = hssfWorkbook.createSheet("Participants sheet")
        val hssfRow = hssfSheet.createRow(0)
        val hssfCellFirstName = hssfRow.createCell(0)
        hssfCellFirstName.setCellValue("Nom")

        val hssfCellLastName = hssfRow.createCell(1)
        hssfCellLastName.setCellValue("Prénom")

        val hssfCellPhone = hssfRow.createCell(2)
        hssfCellPhone.setCellValue("Telephone")

        val hssfCellEmail = hssfRow.createCell(3)
        hssfCellEmail.setCellValue("Email")

        val hssfCellAddress = hssfRow.createCell(4)
        hssfCellAddress.setCellValue("Ville")

        val hssfCellPostalCode = hssfRow.createCell(5)
        hssfCellPostalCode.setCellValue("Code postal")

        val hssfCellBirthDate = hssfRow.createCell(6)
        hssfCellBirthDate.setCellValue("Date de naissance")

        val hssfCellBirthPlace = hssfRow.createCell(7)
        hssfCellBirthPlace.setCellValue("Lieu de naissance")

        val hssfCellResidencePlace = hssfRow.createCell(8)
        hssfCellResidencePlace.setCellValue("Pays de résidence")

        val hssfCellAccpeted = hssfRow.createCell(9)
        hssfCellAccpeted.setCellValue("Plans réductions et communications")

        if (users.isNotEmpty()) {
            var i = 1
            for (user: User in users) {

                val newRow = hssfSheet.createRow(i)
                val newCellFirstName = newRow.createCell(0)
                newCellFirstName.setCellValue(user.lastName)

                val newCellLastName = newRow.createCell(1)
                newCellLastName.setCellValue(user.firstName)

                val newCellPhone = newRow.createCell(2)
                newCellPhone.setCellValue(user.telephone.toString())

                val newCellEmail = newRow.createCell(3)
                newCellEmail.setCellValue(user.email)

                val newCellAddress = newRow.createCell(4)
                newCellAddress.setCellValue(user.ville)

                val newCellPostalCode = newRow.createCell(5)
                newCellPostalCode.setCellValue(user.codePostale)

                val newCellBirthDate = newRow.createCell(6)
                newCellBirthDate.setCellValue(user.birthDate)

                val newCellBirthPlace = newRow.createCell(7)
                newCellBirthPlace.setCellValue(user.birthPlace)

                val newCellResidencePlace = newRow.createCell(8)
                newCellResidencePlace.setCellValue(user.residencePlace)

                val newCellaccepted = newRow.createCell(9)
                newCellaccepted.setCellValue(user.acceptedPlans)
                i++
            }

        }



        try {
            if (!filePath.exists()) {
                filePath.createNewFile()
            }
            val fileOutputStream = FileOutputStream(filePath)
            hssfWorkbook.write(fileOutputStream)

            fileOutputStream.flush()
            fileOutputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun requestWritePermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )
    }


    private fun saveConfig(
        sharedPreferences: SharedPreferences,
        bigPrizeWinners : Long,
        secondPrizeFirstWinners: Long,
        secondPrizeSecondWinners: Long,
        playerswinAfter: Long,
        thirdPrizeFirstWinners: Long,
        thirdPrizeSecondWinners: Long,
        thirdPrizeThirdWinners: Long,
        thirdPrizeFourthWinners: Long,
        morningHours: Long,
        eveningHours: Long

    ) {
        val secondPrizeWinners =secondPrizeFirstWinners+secondPrizeSecondWinners
        val thirdPrizeWinners =thirdPrizeFirstWinners+thirdPrizeSecondWinners+thirdPrizeThirdWinners+thirdPrizeFourthWinners

        sharedPreferences.edit()
            .putLong(KEY_BIG_PRIZE, bigPrizeWinners)
            .putLong(KEY_SECOND_PRIZE, secondPrizeWinners)
            .putLong(KEY_THIRD_PRIZE, thirdPrizeWinners)
            .putLong(KEY_SECOND_PRIZE_FIRST, secondPrizeFirstWinners)
            .putLong(KEY_SECOND_PRIZE_SECOND, secondPrizeSecondWinners)
            .putLong(KEY_WIN_AFTER, playerswinAfter)
            .putLong(KEY_THIRD_PRIZE_FIRST, thirdPrizeFirstWinners)
            .putLong(KEY_THIRD_PRIZE_SECOND, thirdPrizeSecondWinners)
            .putLong(KEY_THIRD_PRIZE_THIRD, thirdPrizeThirdWinners)
            .putLong(KEY_THIRD_PRIZE_FOURTH, thirdPrizeFourthWinners)
            .putLong(KEY_MORNING_HOURS, morningHours)
            .putLong(KEY_EVENING_HOURS, eveningHours)
            .apply()

    }


}