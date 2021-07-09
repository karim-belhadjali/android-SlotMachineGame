package com.nikoarap.slotmachine.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nikoarap.slotmachine.R
import kotlinx.android.synthetic.main.fragment_legal.*
import kotlinx.android.synthetic.main.fragment_you_lost.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LegalFragment : Fragment(R.layout.fragment_legal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_retour_register.setOnClickListener {
            findNavController().navigate(
                R.id.action_legalFragment_to_registerFragment,
                savedInstanceState
            )
        }
    }
}