package com.nikoarap.slotmachine.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nikoarap.slotmachine.R
import kotlinx.android.synthetic.main.fragment_you_lost.*
import kotlinx.android.synthetic.main.fragment_you_won.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class YouLostFragment : Fragment(R.layout.fragment_you_lost) {
    var done = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.IO) {
            delay(5000)
            done=true

        }
        pngegg_2_2.setOnClickListener {
            if(done) {
                findNavController().navigate(
                    R.id.action_youLostFragment_to_thankYouFragment
                )
            }
        }
    }

}