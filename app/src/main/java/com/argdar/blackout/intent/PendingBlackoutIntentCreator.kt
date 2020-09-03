package com.argdar.blackout.intent

import android.app.PendingIntent
import android.content.Context

class PendingBlackoutIntentCreator(private val context: Context) {
    fun create(blackoutDurationMilliseconds: Long, blackoutId: Long) =
        PendingIntent.getBroadcast(
            context,
            1,
            AlarmIntent.create(
                context,
                blackoutDurationMilliseconds,
                blackoutId
            ),
            0)
}