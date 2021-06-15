package com.nikoarap.slotmachine.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingNumber
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingThreeNumber
import com.nikoarap.slotmachine.other.JackpotUtility.randomizeWithEliminatingTwoNumber
import com.nikoarap.slotmachine.slotImageScroll.EventEnd
import com.nikoarap.slotmachine.slotImageScroll.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jackpot.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import kotlin.random.Random

@AndroidEntryPoint
class JackpotFragment : Fragment(R.layout.fragment_jackpot),EventEnd {

    private var countDown = 0

    private var isBiggestPrizeNotWon = MutableLiveData<Boolean>()
    private val halfWorkHours = 4f
    private val timeForOneMassage = 15f
    private var winnersInHalfDay = halfWorkHours * 60f / timeForOneMassage
    private var secondPrizeQuantity = 3f
    private var thirdPrizeQuantity = 2f

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

            if (Utils.score >= 50) {
                leverUp.visibility = View.GONE
                // layout_bar.setBackgroundResource(R.drawable.background2)
                launchTheSlotMachine()
                Utils.score -= 50
                score_tv.text = Utils.score.toString()
            } else {
                Toast.makeText(context, "You don't have enough money", Toast.LENGTH_SHORT).show()
            }
        }
    }


   private fun launchTheSlotMachine() {
        if (isBiggestPrizeNotWon.value == true && winnersInHalfDay != 0f) {
            if (secondPrizeQuantity != 0f) {
                if (thirdPrizeQuantity != 0f) {
                    image1.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                    image2.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                    image3.setRandomValue(
                        Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5
                    )
                } else {
                    image1.setRandomValue(
                        randomizeWithEliminatingNumber(2),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                    image2.setRandomValue(
                        randomizeWithEliminatingNumber(2),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                    image3.setRandomValue(
                        randomizeWithEliminatingNumber(2),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                }

            } else {
                if (thirdPrizeQuantity != 0f) {
                    image1.setRandomValue(
                        randomizeWithEliminatingNumber(3),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                    image2.setRandomValue(
                        randomizeWithEliminatingNumber(3),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                    image3.setRandomValue(
                        Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5
                    )
                } else {
                    image1.setRandomValue(
                        randomizeWithEliminatingTwoNumber(2, 3),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                    image2.setRandomValue(randomizeWithEliminatingTwoNumber(2, 3), Random.nextInt(15 - 5 + 1) + 5)
                    image3.setRandomValue(
                        randomizeWithEliminatingTwoNumber(2, 3),
                        Random.nextInt(15 - 5 + 1) + 5
                    )
                }
            }
            println("${isBiggestPrizeNotWon.value}")
        } else if (secondPrizeQuantity == 0f) {
            if (thirdPrizeQuantity != 0f) {
                image1.setRandomValue(
                    randomizeWithEliminatingTwoNumber(3, 5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
                image2.setRandomValue(
                    randomizeWithEliminatingNumber(5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
                image3.setRandomValue(
                    randomizeWithEliminatingTwoNumber(3, 5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
            } else {
                image1.setRandomValue(
                    randomizeWithEliminatingThreeNumber(3, 2, 5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
                image2.setRandomValue((4..5).random(), Random.nextInt(15 - 5 + 1) + 5)
                image3.setRandomValue(
                    randomizeWithEliminatingThreeNumber(3, 2, 5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
            }
        } else if (secondPrizeQuantity != 0f) {
            if (thirdPrizeQuantity != 0f) {
                image1.setRandomValue(
                    randomizeWithEliminatingNumber(5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
                image2.setRandomValue(
                    randomizeWithEliminatingNumber(5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
                image3.setRandomValue(
                    randomizeWithEliminatingNumber(5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
            } else {
                image1.setRandomValue(
                    randomizeWithEliminatingTwoNumber(2, 5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
                image2.setRandomValue(
                    (4..5).random(),
                    Random.nextInt(15 - 5 + 1) + 5)
                image3.setRandomValue(
                    randomizeWithEliminatingThreeNumber(3, 2, 5),
                    Random.nextInt(15 - 5 + 1) + 5
                )
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
                    5 -> {
                        isBiggestPrizeNotWon.postValue(false)
                        println("Biggest Prize not activated")
                    }
                    3 -> {
                        secondPrizeQuantity-=1
                        println("Second Prize WON")

                    }
                    2 -> {
                        thirdPrizeQuantity-=1
                        println("Third Prize WON")
                    }
                }
                Utils.score += 300
                score_tv.text = Utils.score.toString()
            } else if (image1.value == image2.value || image2.value == image3.value || image1.value == image3.value) {
                Toast.makeText(context, "You did good.", Toast.LENGTH_SHORT).show()
                Utils.score += 100
                score_tv.text = Utils.score.toString()
            } else {
                Toast.makeText(context, "You lost. Better luck next time.", Toast.LENGTH_SHORT).show()
                Utils.score += 0
                score_tv.text = Utils.score.toString()
            }
        }
    }

}