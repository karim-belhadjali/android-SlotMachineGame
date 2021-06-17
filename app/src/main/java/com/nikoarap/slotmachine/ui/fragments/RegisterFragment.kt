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
import com.google.android.material.snackbar.Snackbar
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.db.classes.User
import com.nikoarap.slotmachine.other.Constants
import com.nikoarap.slotmachine.other.Constants.KEY_Email
import com.nikoarap.slotmachine.other.Constants.KEY_NAME
import com.nikoarap.slotmachine.other.Constants.KEY_NEW_USER
import com.nikoarap.slotmachine.other.Constants.KEY_PHONE
import com.nikoarap.slotmachine.ui.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject
import kotlin.math.absoluteValue

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var user: User

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newUser = sharedPreferences.getBoolean(Constants.KEY_NEW_USER, true)


        if (!newUser) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.registerFragment, true)
                .build()

            findNavController().navigate(
                R.id.action_registerFragment_to_jackpotFragment,
                savedInstanceState,
                navOptions
            )
        }




        btn_saveUser.setOnClickListener {

            if (tv_firstName.text.toString()=="" || tv_lastName.text.toString()=="" || tv_email.text.toString()=="" || tv_phone.text.toString()=="" || tv_birthDate.text.toString()=="" || tv_birthPlace.text.toString()=="") {
                Snackbar.make(requireView(), Constants.ENTER_ALL_VALUES, Snackbar.LENGTH_SHORT)
                    .show()
            } else {

            }
            val userFirstName = tv_firstName.text.toString()
            val userLastName = tv_lastName.text.toString()
            val userEmail = tv_email.text.toString()
            val userPhone = tv_phone.text.toString().toLong()
            val userBirthDate = tv_birthDate.text.toString()
            val userBirthPlace = tv_birthPlace.text.toString()

            user=User(userFirstName,userLastName,userEmail,userPhone,userBirthDate,userBirthPlace)

            saveUser(user)

            Snackbar.make(
                requireView(),
                Constants.USER_SAVED_SUCCESSFULLY,
                Snackbar.LENGTH_SHORT
            ).show()

            viewModel.userCount.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                tv_userCount.setText(it.absoluteValue.toString())
            })

           /* findNavController().navigate(
                R.id.action_dashboardFragment_to_jackpotFragment,
                savedInstanceState
            )*/


        }


    }
    private fun saveUser(user: User) {
        viewModel.insertUser(user)
        sharedPreferences.edit()
            .putString(KEY_NAME,user.firstName)
            .putString(KEY_Email,user.email)
            .putLong(KEY_PHONE,user.telephone)
            .putBoolean(KEY_NEW_USER,false)
            .apply()
    }
}


