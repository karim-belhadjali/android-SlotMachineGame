package com.nikoarap.slotmachine

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.nikoarap.slotmachine.slotImageScroll.EventEnd
import com.nikoarap.slotmachine.slotImageScroll.Utils
import kotlinx.android.synthetic.main.main_activity_layout.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), EventEnd {

    private var countDown = 0

    val sdf = SimpleDateFormat("hh:mm:ss")
    val currentTime = sdf.format(Date())
    var timeIsThere = MutableLiveData<Boolean>()
    val halfWorkHours = 4f
    val timeForOneMassage = 15f
    var winnersInHalfDay = halfWorkHours * 60f / timeForOneMassage

    //var winnersEveryHour = winnersInHalfDay / halfWorkHours
    var winnersEveryHour = 5
    var winnersInHour = 0

    //var timeBetweenWinners = winnersEveryHour / 60f
    var timeBetweenWinners = 1f

    private var lastHourTimeStamp = Instant.now()
    private var lastQuarterHourTimeStamp = Instant.now()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        timeIsThere.postValue(true);

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        image1.setEventEnd(this@MainActivity)
        image2.setEventEnd(this@MainActivity)
        image3.setEventEnd(this@MainActivity)

        subscribeToObserves()



        leverUp.setOnClickListener {

            if (Utils.score >= 50) {
                leverUp.visibility = View.GONE
                layout_bar.setBackgroundResource(R.drawable.background2)
                if (timeIsThere.value == true && winnersInHalfDay != 0f) {
                    image1.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                    image2.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                    image3.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                    println("${timeIsThere.value}")
                } else {
                    image1.setRandomValue(Random.nextInt(3), Random.nextInt(15 - 5 + 1) + 5)
                    image2.setRandomValue(Random.nextInt(3), Random.nextInt(15 - 5 + 1) + 5)
                    image3.setRandomValue(Random.nextInt(3), Random.nextInt(15 - 5 + 1) + 5)


                }

                Utils.score -= 50
                score_tv.text = Utils.score.toString()
            } else {
                Toast.makeText(this, "You dont have enough money", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun subscribeToObserves(){
        timeIsThere.observe(this, androidx.lifecycle.Observer {
            possiblityChanged(it)
        })
    }
    fun possiblityChanged(timeIsNow: Boolean) {
        if (!timeIsNow) {
            GlobalScope.launch(Dispatchers.IO) {
                while (winnersInHalfDay != 0f) {
                    var currentHourTimeStamp = Instant.now()
                    var currentQuarterHourTimeStamp = Instant.now()
                    val hour: Duration = Duration.between(lastHourTimeStamp, currentHourTimeStamp)
                    val minutes: Duration =
                        Duration.between(lastQuarterHourTimeStamp, currentQuarterHourTimeStamp)
                    var minutesSince = minutes.toMinutes()
                    var hourSinceWin = hour.toHours()

                    if (hourSinceWin >= 1 && winnersInHalfDay != 0f) {
                        lastHourTimeStamp = Instant.now()
                        timeIsThere.value = true// print after delay
                        println("TIME Hour")
                        winnersInHour = 0

                    } else if (minutesSince >= timeBetweenWinners && winnersInHalfDay != 0f && winnersInHour < winnersEveryHour) {
                        lastQuarterHourTimeStamp = Instant.now()
                        timeIsThere.postValue(true)// print after delay
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
            layout_bar.setBackgroundResource(R.drawable.background1)
            countDown = 0

            if (image1.value == image2.value && image2.value == image3.value) {
                Toast.makeText(this, "YOU WON!!!!", Toast.LENGTH_SHORT).show()
                if (image1.value == 5) {
                    timeIsThere.postValue(false)
                    println("not activated")

                }
                Utils.score += 300
                score_tv.text = Utils.score.toString()
            } else if (image1.value == image2.value || image2.value == image3.value || image1.value == image3.value) {
                Toast.makeText(this, "You did good.", Toast.LENGTH_SHORT).show()
                Utils.score += 100
                score_tv.text = Utils.score.toString()
            } else {
                Toast.makeText(this, "You lost. Better luck next time.", Toast.LENGTH_SHORT).show()
                Utils.score += 0
                score_tv.text = Utils.score.toString()
            }
        }
    }
}
