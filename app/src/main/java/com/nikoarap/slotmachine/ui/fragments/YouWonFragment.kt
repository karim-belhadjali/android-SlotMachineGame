package com.nikoarap.slotmachine.ui.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.other.Constants
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingNumber
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingNumberForThirdPrize
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingThreeNumberForThirdPrize
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingTwoNumber
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingTwoNumberForThirdPrize
import com.nikoarap.slotmachine.slotImageScroll.EventEnd
import com.nikoarap.slotmachine.slotImageScroll.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jackpot.*
import kotlinx.android.synthetic.main.fragment_you_won.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class YouWonFragment : Fragment(R.layout.fragment_you_won) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

        var done = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prizenumber = sharedPreferences.getInt(Constants.KEY_PRIZE, 5)
        val secondPrizeWinners = sharedPreferences.getLong(Constants.KEY_SECOND_PRIZE, 0L)
        val secondPrizeFirstWinners =
            sharedPreferences.getLong(Constants.KEY_SECOND_PRIZE_FIRST, 0L)
        val secondPrizeSecondWinners =
            sharedPreferences.getLong(Constants.KEY_SECOND_PRIZE_SECOND, 0L)
        val secondPrizeThirdWinners =
            sharedPreferences.getLong(Constants.KEY_SECOND_PRIZE_THIRD, 0L)
        val thirdPrizeWinners = sharedPreferences.getLong(Constants.KEY_THIRD_PRIZE, 0L)
        val thirdPrizeFirstWinners = sharedPreferences.getLong(Constants.KEY_THIRD_PRIZE_FIRST, 0L)
        val thirdPrizeSecondWinners =
            sharedPreferences.getLong(Constants.KEY_THIRD_PRIZE_SECOND, 0L)
        val thirdPrizeThirdWinners = sharedPreferences.getLong(Constants.KEY_THIRD_PRIZE_THIRD, 0L)
        val thirdPrizeFourthWinners =
            sharedPreferences.getLong(Constants.KEY_THIRD_PRIZE_FOURTH, 0L)


        sharedPreferences.edit()
            .putLong(Constants.KEY_SECOND_PRIZE_THIRD, 0L)
            .apply()

        when (prizenumber) {
            Utils.orange -> {
                prizeOneWon()
            }
            Utils.lemon -> {
                prizeTwoWon(
                    secondPrizeFirstWinners,
                    secondPrizeSecondWinners,
                    secondPrizeThirdWinners
                )
            }
            Utils.bar -> prizeThreeWon(
                thirdPrizeFirstWinners,
                thirdPrizeSecondWinners,
                thirdPrizeThirdWinners,
                thirdPrizeFourthWinners
            )

        }
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            img_won,
            PropertyValuesHolder.ofFloat("scaleX", 0.5f),
            PropertyValuesHolder.ofFloat("scaleY", 0.5f)
        )
        scaleDown.duration = 2000
        scaleDown.repeatMode = ValueAnimator.REVERSE
        scaleDown.repeatCount = ValueAnimator.INFINITE
        scaleDown.start()

        GlobalScope.launch(Dispatchers.IO) {
            delay(5000)
            done=true

        }
        pngegg_2_1.setOnClickListener {
            if(done) {
                findNavController().navigate(
                    R.id.action_youWonFragment_to_thankYouFragment
                )
            }
        }


    }


    private fun prizeThreeWon(
        thirdPrizeFirstWinners: Long,
        thirdPrizeSecondWinners: Long,
        thirdPrizeThirdWinners: Long,
        thirdPrizeFourthWinners: Long
    ) {
        val randomNumber = randomizeForThirdPrize(
            thirdPrizeFirstWinners,
            thirdPrizeSecondWinners,
            thirdPrizeThirdWinners,
            thirdPrizeFourthWinners
        )

        setImageThirdPrize(
            randomNumber,
            thirdPrizeFirstWinners,
            thirdPrizeSecondWinners,
            thirdPrizeThirdWinners,
            thirdPrizeFourthWinners
        )

    }

    private fun setImageThirdPrize(
        randomNumber: Int,
        thirdPrizeFirstWinners: Long,
        thirdPrizeSecondWinners: Long,
        thirdPrizeThirdWinners: Long,
        thirdPrizeFourthWinners: Long
    ) {
        when (randomNumber) {
            Utils.bar -> {

                img_won!!.setImageResource(R.drawable.casquette)
                val thirdPrizeFirstWinnerss = thirdPrizeFirstWinners - 1L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_THIRD_PRIZE_FIRST, thirdPrizeFirstWinnerss)
                    .apply()
            }
            Utils.lemon -> {
                img_won!!.setImageResource(R.drawable.mdhalla)
                val thirdPrizeSecondWinnerss = thirdPrizeSecondWinners - 1L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_THIRD_PRIZE_SECOND, thirdPrizeSecondWinnerss)
                    .apply()
            }
            Utils.orange -> {
                img_won!!.setImageResource(R.drawable.koura)
                val thirdPrizeThirdWinnerss = thirdPrizeThirdWinners - 1L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_THIRD_PRIZE_THIRD, thirdPrizeThirdWinnerss)
                    .apply()
            }
            Utils.seven -> {
                img_won!!.setImageResource(R.drawable.ri7a)
                val thirdPrizeFourthWinnerss = thirdPrizeFourthWinners - 1L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_THIRD_PRIZE_FOURTH, thirdPrizeFourthWinnerss)
                    .apply()
            }
        }

    }

    private fun prizeTwoWon(
        secondPrizeFirstWinners: Long,
        secondPrizeSecondWinners: Long,
        secondPrizeThirdWinners: Long
    ) {

        var randomnumber = randomizeForSecondPrize(
            secondPrizeFirstWinners,
            secondPrizeSecondWinners,
            secondPrizeThirdWinners
        )
        setImageSecondPrize(
            randomnumber,
            secondPrizeFirstWinners,
            secondPrizeSecondWinners,
            secondPrizeThirdWinners
        )


    }

    private fun setImageSecondPrize(
        randomnumber: Int,
        secondPrizeFirstWinners: Long,
        secondPrizeSecondWinners: Long,
        secondPrizeThirdWinners: Long
    ) {
        when (randomnumber) {
            Utils.bar -> {
                img_won!!.setImageResource(R.drawable.parasol)
                val secondPrizeFirstWinnerss = secondPrizeFirstWinners - 1L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_SECOND_PRIZE_FIRST, secondPrizeFirstWinnerss)
                    .apply()
            }
            Utils.lemon -> {
                img_won!!.setImageResource(R.drawable.car)
                val secondPrizeSecondWinnerss = secondPrizeSecondWinners - 1L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_SECOND_PRIZE_SECOND, secondPrizeSecondWinnerss)
                    .apply()
            }
            Utils.orange -> {
                img_won!!.setImageResource(R.drawable.mouchoir)
                val secondPrizeThirdWinnerss = secondPrizeThirdWinners - 1L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_SECOND_PRIZE_THIRD, secondPrizeThirdWinnerss)
                    .apply()
            }

        }
    }

    private fun randomizeForSecondPrize(
        secondPrizeFirstWinners: Long,
        secondPrizeSecondWinners: Long,
        secondPrizeThirdWinners: Long
    ): Int {
        var randomnumber = 0
        if (secondPrizeFirstWinners != 0L) {
            if (secondPrizeSecondWinners != 0L) {
                if (secondPrizeThirdWinners != 0L) {
                    randomnumber = Random.nextInt(3)
                } else {
                    randomnumber = randomizeWithEliminatingNumber(2)
                }
            } else {
                if (secondPrizeThirdWinners != 0L) {
                    randomnumber = randomizeWithEliminatingNumber(1)
                } else {
                    randomnumber = randomizeWithEliminatingTwoNumber(1, 2)
                }
            }
        } else {
            if (secondPrizeSecondWinners != 0L) {
                if (secondPrizeThirdWinners != 0L) {
                    randomnumber = randomizeWithEliminatingNumber(0)
                } else {
                    randomnumber = randomizeWithEliminatingTwoNumber(0, 2)
                }
            } else {
                if (secondPrizeThirdWinners != 0L) {
                    randomnumber = randomizeWithEliminatingTwoNumber(0, 1)
                }
            }
        }
        return randomnumber
    }

    private fun randomizeForThirdPrize(
        thirdPrizeFirstWinners: Long,
        thirdPrizeSecondWinners: Long,
        thirdPrizeThirdWinners: Long,
        thirdPrizeFourthWinners: Long
    ): Int {
        var randomnumber = 0
        if (thirdPrizeFirstWinners != 0L) {
            if (thirdPrizeSecondWinners != 0L) {
                if (thirdPrizeThirdWinners != 0L) {
                    if (thirdPrizeFourthWinners != 0L) {
                        randomnumber = Random.nextInt(4)
                    } else {
                        randomizeWithEliminatingNumberForThirdPrize(3)
                    }
                } else {
                    if (thirdPrizeFourthWinners != 0L) {
                        randomizeWithEliminatingNumberForThirdPrize(2)
                    } else {
                        randomizeWithEliminatingTwoNumberForThirdPrize(2, 3)
                    }
                }
            } else {
                if (thirdPrizeThirdWinners != 0L) {
                    if (thirdPrizeFourthWinners != 0L) {
                        randomizeWithEliminatingNumberForThirdPrize(1)
                    } else {
                        randomizeWithEliminatingTwoNumberForThirdPrize(1, 3)
                    }
                } else {
                    if (thirdPrizeFourthWinners != 0L) {
                        randomizeWithEliminatingTwoNumberForThirdPrize(1, 2)
                    } else {
                        randomizeWithEliminatingThreeNumberForThirdPrize(1, 2, 3)
                    }
                }
            }
        } else {
            if (thirdPrizeSecondWinners != 0L) {
                if (thirdPrizeThirdWinners != 0L) {
                    if (thirdPrizeFourthWinners != 0L) {
                        randomizeWithEliminatingNumberForThirdPrize(0)
                    } else {
                        randomizeWithEliminatingTwoNumberForThirdPrize(0, 3)
                    }
                } else {
                    if (thirdPrizeFourthWinners != 0L) {
                        randomizeWithEliminatingTwoNumberForThirdPrize(0, 2)
                    } else {
                        randomizeWithEliminatingThreeNumberForThirdPrize(0, 2, 3)
                    }
                }
            } else {
                if (thirdPrizeThirdWinners != 0L) {
                    if (thirdPrizeFourthWinners != 0L) {
                        randomizeWithEliminatingTwoNumberForThirdPrize(0, 1)
                    } else {
                        randomizeWithEliminatingThreeNumberForThirdPrize(0, 1, 3)
                    }
                } else {
                    if (thirdPrizeFourthWinners != 0L) {
                        randomizeWithEliminatingThreeNumberForThirdPrize(0, 1, 2)
                    }
                }
            }
        }
        return randomnumber
    }

    private fun prizeOneWon() {
        img_won!!.setImageResource(R.drawable.massage)
    }
}