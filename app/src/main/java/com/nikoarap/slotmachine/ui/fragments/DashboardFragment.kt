package com.nikoarap.slotmachine.ui.fragments

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_TOGGLE_FIRST_TIME
import com.nikoarap.slotmachine.ui.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
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
    lateinit var users: List<User>

    private val CSV_HEADER = "id, FirstName, LastName, Email, Telephone , BirthDate, BirthPlace"

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
            users = it
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
            createExcel(users)
        }

        btn_retour.setOnClickListener {

            findNavController().navigate(
                R.id.action_dashboardFragment_to_registerFragment,
                savedInstanceState
            )
        }
    }

    private fun exportCSV() {
        val path = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        println(path)

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

            val filePath = File(Environment.getExternalStorageDirectory().toString() + "/Users.xls")
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
            hssfCellAddress.setCellValue("Adresse")

            val hssfCellPostalCode = hssfRow.createCell(5)
            hssfCellPostalCode.setCellValue("Code postal")

            val hssfCellBirthDate = hssfRow.createCell(6)
            hssfCellBirthDate.setCellValue("Date de naissance")

            val hssfCellBirthPlace = hssfRow.createCell(7)
            hssfCellBirthPlace.setCellValue("Lieu de naissance")

            if (users.isNotEmpty()) {

                for ((i, user: User) in users.withIndex()) {

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
                    newCellAddress.setCellValue("Adresse")

                    val newCellPostalCode = newRow.createCell(5)
                    newCellPostalCode.setCellValue("Code postal")

                    val newCellBirthDate = newRow.createCell(6)
                    newCellBirthDate.setCellValue(user.birthDate)

                    val newCellBirthPlace = newRow.createCell(7)
                    newCellBirthPlace.setCellValue(user.birthPlace)

                }
                println("done")
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