package com.argdar.blackout.intent

import android.content.Context
import android.content.Intent
import com.argdar.blackout.receiver.AlarmReceiver

object AlarmIntent {
    const val BLACKOUT_ID = "blackout_id"
    const val BLACKOUT_TIME_KEY = "blackout_time"

    fun create(context: Context, blackoutTime: Long, blackoutId: Long): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(BLACKOUT_TIME_KEY, blackoutTime)
        intent.putExtra(BLACKOUT_ID, blackoutId)
        return intent
    }
}