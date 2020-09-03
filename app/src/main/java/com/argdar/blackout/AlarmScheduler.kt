package com.argdar.blackout

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import com.argdar.blackout.intent.PendingBlackoutIntentCreator

class AlarmScheduler(
    private val alarmManager: AlarmManager,
    private val pendingBlackoutIntentCreator: PendingBlackoutIntentCreator) {

    fun schedule(blackout: ScheduledBlackout) {
        alarmManager.set(RTC_WAKEUP,
            blackout.timestamp,
            pendingBlackoutIntentCreator.create(
                blackout.duration,
                blackout.id
            ))
    }
}
