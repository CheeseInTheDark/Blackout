package com.argdar.blackout

import android.media.AudioAttributes
import android.media.AudioAttributes.USAGE_ALARM

class AlarmAudioAttributesCreator {
    fun create(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(USAGE_ALARM)
            .build()
    }
}
