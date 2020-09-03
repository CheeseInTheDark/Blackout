package com.argdar.blackout.intent

import android.app.PendingIntent
import android.content.Context
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class PendingRegretIntentCreator(private val context: Context) : KoinComponent {
    fun create(): PendingIntent {
        val regrettableIntent: RegrettableIntent by inject { parametersOf(context) }
        return PendingIntent.getBroadcast(context, 2, regrettableIntent, 0)
    }
}