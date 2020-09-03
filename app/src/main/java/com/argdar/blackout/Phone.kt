package com.argdar.blackout

import android.content.Context
import com.argdar.blackout.intent.PhoneCallIntentCreator
import org.koin.core.KoinComponent

class Phone(
    private val context: Context,
    private val phoneCallIntentCreator: PhoneCallIntentCreator
) : KoinComponent {
    fun call(number: String) {
        context.startActivity(phoneCallIntentCreator.create(number))
    }
}