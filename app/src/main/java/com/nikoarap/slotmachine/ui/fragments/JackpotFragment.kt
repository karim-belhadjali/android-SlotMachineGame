package com.nikoarap.slotmachine.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.other.Constants
import com.nikoarap.slotmachine.other.Constants.KEY_BIG_PRIZE_WON
import com.nikoarap.slotmachine.other.Constants.KEY_LAST_QUARTER
import com.nikoarap.slotmachine.other.Constants.KEY_PRIZE
import com.nikoarap.slotmachine.other.Constants.KEY_SECOND_PRIZE_WON
import com.nikoarap.slotmachine.other.Constants.KEY_THIRD_PRIZE_WON
import com.nikoarap.slotmachine.other.Constants.KEY_WINNERS_BIG_PRIZE
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingNumber
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingThreeNumber
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingTwoNumber
import com.nikoarap.slotmachine.slotImageScroll.EventEnd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jackpot.*
import kotlinx.coroutines.*
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class JackpotFragment : Fragment(R.layout.fragment_jackpot), EventEnd {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var countDown = 0

    private var isBiggestPrizeNotWon = MutableLiveData<Boolean>()
    private var halfWorkHours = 0L
    private val timeForOneMassage = 15L
    private var winnersInHalfDay = 0L
    private var bigPrizeQuantity = 0L
    private var secondPrizeQuantity = 0L
    private var thirdPrizeQuantity = 0L
    private var playersWinAfter = 0L
    private var playersNotWon = 0L
    private var winnersofBigPrize = 0L
    private var lastQuarterHourTimeStamp = 0L
    private var lastQuarterHourWon = LocalDateTime.now()
    private var secondPrizeWon = false
    private var thirdPrizeWon = false
    var localDateTime = LocalDateTime.now()
    var formatter = DateTimeFormatter.ofPattern("HH")
    var output = formatter.format(localDateTime)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentQuarterHourTimeStamp = Instant.now().toEpochMilli()
        val minute = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(currentQuarterHourTimeStamp),
            ZoneId.systemDefault()
        )
        jackpot_1.setBackgroundResource(R.drawable.jackpot_1)
        jackpot_1.setBackgroundResource(R.drawable.jackpot_1)
        jackpot_1.setBackgroundResource(R.drawable.jackpot_1)


        lastQuarterHourTimeStamp = sharedPreferences.getLong(KEY_LAST_QUARTER, 0L)
        lastQuarterHourWon = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(lastQuarterHourTimeStamp),
            ZoneId.systemDefault()
        )

        if (output > 12.toString()) {
            halfWorkHours = sharedPreferences.getLong(Constants.KEY_EVENING_HOURS, 0L)
        } else {
            halfWorkHours = sharedPreferences.getLong(Constants.KEY_MORNING_HOURS, 0L)
        }
        winnersInHalfDay = halfWorkHours * 60L / timeForOneMassage
        winnersofBigPrize = sharedPreferences.getLong(KEY_WINNERS_BIG_PRIZE, 0L)
        bigPrizeQuantity = sharedPreferences.getLong(Constants.KEY_BIG_PRIZE, 0L)
        secondPrizeQuantity = sharedPreferences.getLong(Constants.KEY_SECOND_PRIZE, 0L)
        thirdPrizeQuantity = sharedPreferences.getLong(Constants.KEY_THIRD_PRIZE, 0L)
        playersWinAfter = sharedPreferences.getLong(Constants.KEY_WIN_AFTER, 0L)
        playersNotWon = sharedPreferences.getLong(Constants.KEY_NOT_WON, 0L)
        secondPrizeWon = sharedPreferences.getBoolean(KEY_SECOND_PRIZE_WON, false)
        thirdPrizeWon = sharedPreferences.getBoolean(KEY_THIRD_PRIZE_WON, false)

        val won = sharedPreferences.getBoolean(KEY_BIG_PRIZE_WON, true)
        isBiggestPrizeNotWon.postValue(won)

        if(!(secondPrizeQuantity>0)&& secondPrizeWon){
            if (thirdPrizeQuantity>0) {
                secondPrizeWon =false
                thirdPrizeWon = true
            }else{
                secondPrizeWon = false
                thirdPrizeWon = false
            }
        }

        if(!(thirdPrizeQuantity>0)&& thirdPrizeWon){
            if (secondPrizeQuantity>0) {
                secondPrizeWon = true
                thirdPrizeWon = false
            }else{
                secondPrizeWon = false
                thirdPrizeWon = false
            }
        }

        if (!secondPrizeWon&& !thirdPrizeWon){
            if (thirdPrizeQuantity>0){
                thirdPrizeWon=true
            }
            if (secondPrizeQuantity>0){
                secondPrizeWon=true
            }
        }




        val minutes: Duration =
            Duration.between(lastQuarterHourWon, minute)
        println("last time chair won:   " + minutes.toMinutes().toInt())
        image1.setEventEnd(this)
        image2.setEventEnd(this)
        image3.setEventEnd(this)

        subscribeToObserves()
        println("players not won:  " + playersNotWon)
        println("players winn after:  " + playersWinAfter)
        println("second prize won:  " + secondPrizeWon)
        println("third prize won:  " + thirdPrizeWon)

        println("Is the number of the players not won already reached:  ${playersNotWon >= playersWinAfter}")

        leverUp.setOnClickListener {

            leverUp.visibility = View.GONE
            jackpot_1.setBackgroundResource(R.drawable.jackpot_down)
            jackpot_1.setBackgroundResource(R.drawable.jackpot_down)
            jackpot_1.setBackgroundResource(R.drawable.jackpot_down)
            launchTheSlotMachine()

        }
    }


    private fun launchTheSlotMachine() {
        if (playersNotWon >= playersWinAfter) {

            if (checkIfBigPrizeIsAvailable()) {
                println("Going to win Big prize")
                image1.setRandomValue(2, Random.nextInt(15 - 5 + 1) + 5)
                image2.setRandomValue(2, Random.nextInt(15 - 5 + 1) + 5)
                image3.setRandomValue(2, Random.nextInt(15 - 5 + 1) + 5)
                playersNotWon = 0L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_NOT_WON, playersNotWon)
                    .apply()
            } else if (checkIfSecondPrizeIsAvailable() && secondPrizeWon) {
                println("Going to win second prize")

                image1.setRandomValue(1, Random.nextInt(15 - 5 + 1) + 5)
                image2.setRandomValue(1, Random.nextInt(15 - 5 + 1) + 5)
                image3.setRandomValue(1, Random.nextInt(15 - 5 + 1) + 5)
                playersNotWon = 0L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_NOT_WON, playersNotWon)
                    .apply()
                    sharedPreferences.edit()
                        .putBoolean(KEY_SECOND_PRIZE_WON, false)
                        .putBoolean(KEY_THIRD_PRIZE_WON, true)
                        .apply()

            } else if (checkIfThirdPrizeIsAvailable() && thirdPrizeWon) {
                println("Going to win third prize")

                image1.setRandomValue(0, Random.nextInt(15 - 5 + 1) + 5)
                image2.setRandomValue(0, Random.nextInt(15 - 5 + 1) + 5)
                image3.setRandomValue(0, Random.nextInt(15 - 5 + 1) + 5)
                playersNotWon = 0L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_NOT_WON, playersNotWon)
                    .apply()

                    sharedPreferences.edit()
                        .putBoolean(KEY_SECOND_PRIZE_WON, true)
                        .putBoolean(KEY_THIRD_PRIZE_WON, false)
                        .apply()

            } else {
                launchWhenNoPrizeIsAvailable()
            }
        } else {


            if (checkIfBigPrizeIsAvailable()) {
                if (checkIfSecondPrizeIsAvailable()) {

                    if (checkIfThirdPrizeIsAvailable()) {

                        launchWhenAllPrizesAvailable()
                    } else {
                        launchWhenOnlyThirdPrizeIsNotAvailable()
                    }

                } else {
                    if (checkIfThirdPrizeIsAvailable()) {

                        launchWhenOnlySecondPrizeIsNotAvailable()
                    } else {
                        launchWhenThirdAndSecondPrizeIsNotAvailable()
                    }
                }
            } else if (!checkIfSecondPrizeIsAvailable()) {

                if (checkIfThirdPrizeIsAvailable()) {

                    launchWhenFirstAndSecondPrizeIsNotAvailable()
                } else {
                    launchWhenNoPrizeIsAvailable()
                }
            } else if (checkIfSecondPrizeIsAvailable()) {

                if (checkIfThirdPrizeIsAvailable()) {

                    launchWhenOnlyFirstPrizeIsNotAvailable()
                } else {
                    launchWhenOnlyFirstAndThirdPrizeIsNotAvailable()
                }
            }
        }

    }

    private fun subscribeToObserves() {
        isBiggestPrizeNotWon.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            biggestPrizeCombination(sharedPreferences.getBoolean(KEY_BIG_PRIZE_WON, true))
            println(
                "First check for Big Prize is notWon:  " + sharedPreferences.getBoolean(
                    KEY_BIG_PRIZE_WON,
                    true
                )
            )
        })
    }

    private fun biggestPrizeCombination(timeIsNow: Boolean) {
        var minutesSince = 0
        if (!timeIsNow) {
            if (lastQuarterHourTimeStamp != 0L) {
                val currentQuarterHourTimeStamp = Instant.now().toEpochMilli()
                val minute = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(currentQuarterHourTimeStamp),
                    ZoneId.systemDefault()
                )

                val minutes: Duration =
                    Duration.between(lastQuarterHourWon, minute)
                minutesSince = minutes.toMinutes().toInt()
            } else {
                minutesSince = timeForOneMassage.toInt()
            }

            if (minutesSince >= timeForOneMassage && winnersInHalfDay > winnersofBigPrize) {
                isBiggestPrizeNotWon.postValue(true)// print after delay
                sharedPreferences.edit()
                    .putBoolean(KEY_BIG_PRIZE_WON, true)
                    .apply()
                println(" is 15min passed to win  :${isBiggestPrizeNotWon.value}")
            }
        }
    }


    override fun eventEnd(result: Int, count: Int) {
        if (countDown < 2) {
            countDown++
        } else {
            leverUp.visibility = View.VISIBLE
            jackpot_1.setBackgroundResource(R.drawable.jackpot_1)
            jackpot_1.setBackgroundResource(R.drawable.jackpot_1)
            jackpot_1.setBackgroundResource(R.drawable.jackpot_1)
            countDown = 0

            if (image1.value == image2.value && image2.value == image3.value) {
                jackpot_1.setBackgroundResource(R.drawable.jackpot_1)
                when (image1.value) {
                    2 -> {
                        winnersofBigPrize += 1L
                        sharedPreferences.edit()
                            .putBoolean(KEY_BIG_PRIZE_WON, false)
                            .putLong(KEY_LAST_QUARTER, Instant.now().toEpochMilli())
                            .putLong(KEY_WINNERS_BIG_PRIZE, winnersofBigPrize)
                            .apply()
                        bigPrizeQuantity -= 1
                        sharedPreferences.edit()
                            .putLong(Constants.KEY_BIG_PRIZE, bigPrizeQuantity)
                            .apply()
                    }
                    1 -> {
                        secondPrizeQuantity -= 1
                        sharedPreferences.edit()
                            .putLong(Constants.KEY_SECOND_PRIZE, secondPrizeQuantity)
                            .apply()

                    }
                    0 -> {
                        thirdPrizeQuantity -= 1
                        sharedPreferences.edit()
                            .putLong(Constants.KEY_THIRD_PRIZE, thirdPrizeQuantity)
                            .apply()
                    }
                }

                playersNotWon = 0L

                sharedPreferences.edit()
                    .putInt(KEY_PRIZE, image1.value)
                    .putLong(Constants.KEY_NOT_WON, playersNotWon)
                    .apply()

                GlobalScope.launch(Dispatchers.IO) {
                    delay(2000)
                    findNavController().navigate(
                        R.id.action_jackpotFragment_to_youWonFragment
                    )
                }

            } else {
                jackpot_1.setBackgroundResource(R.drawable.jackpot_1)
                playersNotWon += 1L
                sharedPreferences.edit()
                    .putLong(Constants.KEY_NOT_WON, playersNotWon)
                    .apply()

                GlobalScope.launch(Dispatchers.IO) {
                    delay(2000)
                    findNavController().navigate(
                        R.id.action_jackpotFragment_to_youLostFragment
                    )
                }
            }
        }
    }

    private fun checkIfBigPrizeIsAvailable(): Boolean {
        val khra =
            isBiggestPrizeNotWon.value == true && winnersInHalfDay > 0 && bigPrizeQuantity > 0
        println("Big prize check :   " + khra)
        return khra

    }

    private fun checkIfSecondPrizeIsAvailable(): Boolean {
        println("second prize check :   ${secondPrizeQuantity > 0}")
        return secondPrizeQuantity > 0
    }

    private fun checkIfThirdPrizeIsAvailable(): Boolean {
        println("third prize check :    ${thirdPrizeQuantity > 0}")
        return thirdPrizeQuantity > 0
    }

    private fun launchWhenAllPrizesAvailable() {
        image1.setRandomValue(Random.nextInt(3), Random.nextInt(15 - 5 + 1) + 5)
        image2.setRandomValue(Random.nextInt(3), Random.nextInt(15 - 5 + 1) + 5)
        image3.setRandomValue(Random.nextInt(3), Random.nextInt(15 - 5 + 1) + 5)
    }

    private fun launchWhenOnlyThirdPrizeIsNotAvailable() {
        image1.setRandomValue(randomizeWithEliminatingNumber(0), Random.nextInt(15 - 5 + 1) + 5)
        image2.setRandomValue(randomizeWithEliminatingNumber(0), Random.nextInt(15 - 5 + 1) + 5)
        image3.setRandomValue(randomizeWithEliminatingNumber(0), Random.nextInt(15 - 5 + 1) + 5)
    }

    private fun launchWhenOnlySecondPrizeIsNotAvailable() {
        image1.setRandomValue(randomizeWithEliminatingNumber(1), Random.nextInt(15 - 5 + 1) + 5)
        image2.setRandomValue(randomizeWithEliminatingNumber(1), Random.nextInt(15 - 5 + 1) + 5)
        image3.setRandomValue(randomizeWithEliminatingNumber(1), Random.nextInt(15 - 5 + 1) + 5)
    }

    private fun launchWhenThirdAndSecondPrizeIsNotAvailable() {
        image1.setRandomValue(
            randomizeWithEliminatingTwoNumber(0, 1),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image2.setRandomValue(
            randomizeWithEliminatingTwoNumber(0, 1),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image3.setRandomValue(
            randomizeWithEliminatingTwoNumber(0, 1),
            Random.nextInt(15 - 5 + 1) + 5
        )
    }

    private fun launchWhenFirstAndSecondPrizeIsNotAvailable() {
        image1.setRandomValue(
            randomizeWithEliminatingTwoNumber(2, 1),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image2.setRandomValue(
            randomizeWithEliminatingTwoNumber(2, 1),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image3.setRandomValue(
            randomizeWithEliminatingTwoNumber(2, 1),
            Random.nextInt(15 - 5 + 1) + 5
        )
    }

    private fun launchWhenNoPrizeIsAvailable() {
        image1.setRandomValue(
            randomizeWithEliminatingTwoNumber(2, 1),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image2.setRandomValue(
            randomizeWithEliminatingTwoNumber(2, 0),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image3.setRandomValue(
            randomizeWithEliminatingTwoNumber(0, 1),
            Random.nextInt(15 - 5 + 1) + 5
        )
    }

    private fun launchWhenOnlyFirstPrizeIsNotAvailable() {
        image1.setRandomValue(randomizeWithEliminatingNumber(3), Random.nextInt(15 - 5 + 1) + 5)
        image2.setRandomValue(randomizeWithEliminatingNumber(3), Random.nextInt(15 - 5 + 1) + 5)
        image3.setRandomValue(randomizeWithEliminatingNumber(3), Random.nextInt(15 - 5 + 1) + 5)
    }

    private fun launchWhenOnlyFirstAndThirdPrizeIsNotAvailable() {
        image1.setRandomValue(
            randomizeWithEliminatingTwoNumber(0, 3),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image2.setRandomValue(
            randomizeWithEliminatingTwoNumber(0, 3),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image3.setRandomValue(
            randomizeWithEliminatingTwoNumber(0, 3),
            Random.nextInt(15 - 5 + 1) + 5
        )
    }


}