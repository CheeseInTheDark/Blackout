package com.argdar.blackout.intent

import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import org.koin.core.KoinComponent
import org.koin.core.inject

class PhoneCallIntentCreator : KoinComponent {
    fun create(number: String) : Intent {
        val phoneCallIntent by inject<Intent>()
        phoneCallIntent.data = Uri.parse("tel:$number")
        phoneCallIntent.action = ACTION_CALL
        phoneCallIntent.flags = FLAG_ACTIVITY_NEW_TASK
        return phoneCallIntent
    }
}