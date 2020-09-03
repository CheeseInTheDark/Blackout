package com.argdar.blackout

import android.content.ComponentName
import android.content.Context
import com.argdar.blackout.receiver.AlarmRescheduleReceiver

class AlarmReschedulerComponentNameCreator(private val context: Context) {
    fun create(): ComponentName {
        return ComponentName(context, AlarmRescheduleReceiver::class.java)
    }
}