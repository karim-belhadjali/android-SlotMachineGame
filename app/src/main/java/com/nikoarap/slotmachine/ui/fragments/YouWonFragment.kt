package com.nikoarap.slotmachine.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.other.Constants
import com.nikoarap.slotmachine.slotImageScroll.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_thank_you.*
import kotlinx.android.synthetic.main.fragment_you_won.*
import javax.inject.Inject

@AndroidEntryPoint
class YouWonFragment : Fragment(R.layout.fragment_you_won) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = sharedPreferences.getString(Constants.KEY_NAME, "")
        val prizenumber = sharedPreferences.getInt(Constants.KEY_PRIZE, 5)

        when (prizenumber) {
            Utils.bar -> img_won!!.setImageResource(R.drawable.bar)
            Utils.lemon -> img_won!!.setImageResource(R.drawable.lemon)
            Utils.orange -> img_won!!.setImageResource(R.drawable.orange)
            Utils.seven -> img_won!!.setImageResource(R.drawable.seven)
            Utils.triple -> img_won!!.setImageResource(R.drawable.triple_seven)
            Utils.watermelon -> img_won!!.setImageResource(R.drawable.watermelon)
        }


        btn_gotToThankYou.setOnClickListener {

            findNavController().navigate(
                R.id.action_youWonFragment_to_thankYouFragment,
                savedInstanceState

            )
        }
    }
}