package com.argdar.blackout.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.argdar.blackout.AlarmScheduler
import com.argdar.blackout.PersistentAlarmTracker
import org.koin.core.KoinComponent
import org.koin.core.get

class AlarmRescheduleReceiver : KoinComponent, BroadcastReceiver() {

    val persistentAlarmTracker: PersistentAlarmTracker = get()
    val alarmScheduler: AlarmScheduler = get()

    override fun onReceive(context: Context?, intent: Intent?) {
    if (Intent.ACTION_BOOT_COMPLETED != intent?.action) return

        persistentAlarmTracker.getAll().forEach(alarmScheduler::schedule)
    }
}