package com.argdar.blackout

import kotlin.random.Random

class BlackoutStartTimePicker(private val random: Random) {
    fun pickBetween(timeRangeBegin: Long, timeRangeEnd: Long): Long {
        return if (timeRangeBegin < timeRangeEnd) random.nextLong(timeRangeBegin, timeRangeEnd + 1) else timeRangeBegin
    }
}
