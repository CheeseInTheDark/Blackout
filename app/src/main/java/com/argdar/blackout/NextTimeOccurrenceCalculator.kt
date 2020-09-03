package com.argdar.blackout

class NextTimeOccurrenceCalculator(private val systemClock: SystemClock) {
    val msPerDay = 86400000L
    val msPerHour = 3600000L
    val msPerMinute = 60000L

    fun getNextTimestamp(hour: Int, minute: Int): Long {
        val timezoneOffset = systemClock.timeFromGMT()
        val currentTime = systemClock.currentTimeMillis()
        val timeOfDay = (currentTime + timezoneOffset) % msPerDay
        val currentDay = currentTime + timezoneOffset - timeOfDay
        val requestedTimeOfDay = minute * msPerMinute + hour * msPerHour

        return currentDay - timezoneOffset + if (requestedTimeOfDay < timeOfDay)
            msPerDay + requestedTimeOfDay
        else
            requestedTimeOfDay
    }
}