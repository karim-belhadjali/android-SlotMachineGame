package com.nikoarap.slotmachine.other

import kotlin.random.Random

object JackpotUtility {
     fun randomizeWithEliminatingNumber(num: Int): Int {
        var number = Random.nextInt(3)
        return if (number != num) {
            return number
        } else {
            randomizeWithEliminatingNumber(num)
        }
    }

     fun randomizeWithEliminatingTwoNumber(num: Int, num2: Int): Int {
        var number = Random.nextInt(3)
        return if (number != num && number != num2) {
            return number
        } else {
            randomizeWithEliminatingTwoNumber(num, num2)
        }
    }

    fun randomizeWithEliminatingThreeNumber(num: Int, num2: Int, num3: Int): Int {
        var number = Random.nextInt(3)
        return if (number != num && number != num2 && number != num3) {
            return number
        } else {
            randomizeWithEliminatingThreeNumber(num, num2, num3)
        }
    }
}