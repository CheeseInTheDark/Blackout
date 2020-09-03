package com.argdar.blackout.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.argdar.blackout.PersistentAlarmTracker
import com.argdar.blackout.intent.AlarmActivityIntentCreator
import com.argdar.blackout.intent.AlarmIntent.BLACKOUT_ID
import com.argdar.blackout.intent.AlarmIntent.BLACKOUT_TIME_KEY
import org.koin.core.KoinComponent
import org.koin.core.get

class AlarmReceiver : KoinComponent, BroadcastReceiver() {
    private val alarmActivityIntentCreator: AlarmActivityIntentCreator = get()
    private val persistentAlarmTracker: PersistentAlarmTracker = get()

    override fun onReceive(context: Context, intent: Intent) {
        val blackoutId = intent.getLongExtra(BLACKOUT_ID, 0)
        persistentAlarmTracker.remove(blackoutId)

        val blackoutStartIntent = alarmActivityIntentCreator.create(intent.getLongExtra(BLACKOUT_TIME_KEY, 0))
        context.startActivity(blackoutStartIntent)
    }
}