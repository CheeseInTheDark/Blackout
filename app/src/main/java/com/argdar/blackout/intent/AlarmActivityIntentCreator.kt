package com.argdar.blackout.intent

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.argdar.blackout.activity.AlarmActivity
import com.argdar.blackout.intent.AlarmIntent.BLACKOUT_TIME_KEY
import org.koin.core.KoinComponent
import org.koin.core.inject

class AlarmActivityIntentCreator(private val context: Context) : KoinComponent {
    fun create(blackoutDuration: Long) : Intent {
        val intent by inject<Intent>()
        intent.setClass(context, AlarmActivity::class.java)
        intent.putExtra(BLACKOUT_TIME_KEY, blackoutDuration)
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        return intent
    }
}