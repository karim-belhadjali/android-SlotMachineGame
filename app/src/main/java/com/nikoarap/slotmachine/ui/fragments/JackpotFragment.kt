package com.nikoarap.slotmachine.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.other.Constants.KEY_PRIZE
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingNumber
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingThreeNumber
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingTwoNumber
import com.nikoarap.slotmachine.slotImageScroll.EventEnd
import com.nikoarap.slotmachine.slotImageScroll.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jackpot.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class JackpotFragment : Fragment(R.layout.fragment_jackpot), EventEnd {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var countDown = 0

    private var isBiggestPrizeNotWon = MutableLiveData<Boolean>()
    private val halfWorkHours = 4f
    private val timeForOneMassage = 15f
    private var winnersInHalfDay = halfWorkHours * 60f / timeForOneMassage
    private var secondPrizeQuantity = 1f
    private var thirdPrizeQuantity = 1f

    //var winnersEveryHour = winnersInHalfDay / halfWorkHours
    private var winnersEveryHour = 5
    private var winnersInHour = 0

    //var timeBetweenWinners = winnersEveryHour / 60f
    private var timeBetweenWinners = 1f

    private var lastHourTimeStamp = Instant.now()
    private var lastQuarterHourTimeStamp = Instant.now()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBiggestPrizeNotWon.postValue(true)

        image1.setEventEnd(this)
        image2.setEventEnd(this)
        image3.setEventEnd(this)

        subscribeToObserves()

        leverUp.setOnClickListener {

                leverUp.visibility = View.GONE
                launchTheSlotMachine()

        }
    }


    private fun launchTheSlotMachine() {
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
            println("${isBiggestPrizeNotWon.value}")
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

    private fun subscribeToObserves() {
        isBiggestPrizeNotWon.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            biggestPrizeCombination(it)
        })
    }

    private fun biggestPrizeCombination(timeIsNow: Boolean) {
        if (!timeIsNow) {
            GlobalScope.launch(Dispatchers.IO) {
                while (winnersInHalfDay != 0f) {
                    val currentHourTimeStamp = Instant.now()
                    val currentQuarterHourTimeStamp = Instant.now()
                    val hour: Duration = Duration.between(lastHourTimeStamp, currentHourTimeStamp)
                    val minutes: Duration =
                        Duration.between(lastQuarterHourTimeStamp, currentQuarterHourTimeStamp)
                    val minutesSince = minutes.toMinutes()
                    val hourSinceWin = hour.toHours()

                    if (hourSinceWin >= 1 && winnersInHalfDay != 0f) {
                        lastHourTimeStamp = Instant.now()
                        isBiggestPrizeNotWon.value = true// print after delay
                        println("TIME Hour")
                        winnersInHour = 0

                    } else if (minutesSince >= timeBetweenWinners && winnersInHalfDay != 0f && winnersInHour < winnersEveryHour) {
                        lastQuarterHourTimeStamp = Instant.now()
                        isBiggestPrizeNotWon.postValue(true)// print after delay
                        winnersInHalfDay -= 1
                        winnersInHour += 1
                        println("TIME minutes")
                    }
                }
            }
        }
    }


    override fun eventEnd(result: Int, count: Int) {
        if (countDown < 2) {
            countDown++
        } else {
            leverUp.visibility = View.VISIBLE
            // layout_bar.setBackgroundResource(R.drawable.background1)
            countDown = 0

            if (image1.value == image2.value && image2.value == image3.value) {
                Toast.makeText(context, "YOU WON!!!!", Toast.LENGTH_SHORT).show()
                when (image1.value) {
                    2 -> {
                        isBiggestPrizeNotWon.postValue(false)
                    }
                    1 -> {
                        secondPrizeQuantity -= 1

                    }
                    0 -> {
                        thirdPrizeQuantity -= 1
                    }
                }

                sharedPreferences.edit()
                    .putInt(KEY_PRIZE, image1.value)
                    .apply()

                GlobalScope.launch(Dispatchers.IO) {
                    delay(2000)
                    findNavController().navigate(
                        R.id.action_jackpotFragment_to_youWonFragment
                    )
                }

            } else if (image1.value == image2.value || image2.value == image3.value || image1.value == image3.value) {

                GlobalScope.launch(Dispatchers.IO) {
                    delay(2000)
                    findNavController().navigate(
                        R.id.action_jackpotFragment_to_thankYouFragment
                    )
                }

            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    delay(2000)
                    findNavController().navigate(
                        R.id.action_jackpotFragment_to_thankYouFragment
                    )
                }
            }
        }
    }

    private fun checkIfBigPrizeIsAvailable(): Boolean {
        return isBiggestPrizeNotWon.value == true && winnersInHalfDay != 0f
    }

    private fun checkIfSecondPrizeIsAvailable(): Boolean {
        return secondPrizeQuantity != 0f
    }

    private fun checkIfThirdPrizeIsAvailable(): Boolean {
        return thirdPrizeQuantity != 0f
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
            randomizeWithEliminatingThreeNumber(0, 1, 2),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image2.setRandomValue(
            randomizeWithEliminatingThreeNumber(0, 1, 2),
            Random.nextInt(15 - 5 + 1) + 5
        )
        image3.setRandomValue(
            randomizeWithEliminatingThreeNumber(0, 1, 2),
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