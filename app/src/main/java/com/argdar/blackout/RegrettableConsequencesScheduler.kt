package com.argdar.blackout

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import com.argdar.blackout.intent.PendingRegretIntentCreator

class RegrettableConsequencesScheduler(
    private val alarmManager: AlarmManager,
    private val pendingRegretIntentCreator: PendingRegretIntentCreator,
    private val systemClock: SystemClock
) {
    fun scheduleRegrets(durationInMs: Long) {

        val occurrences = durationInMs / 15000L
        val startTime = systemClock.currentTimeMillis()

        for (occurrence in 0..occurrences) {
            alarmManager.set(RTC_WAKEUP, startTime + 15000L * occurrence, pendingRegretIntentCreator.create())
        }
    }
}