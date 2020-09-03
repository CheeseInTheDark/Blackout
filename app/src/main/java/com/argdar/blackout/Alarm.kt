package com.argdar.blackout

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.RingtoneManager.TYPE_ALARM
import android.media.RingtoneManager.TYPE_NOTIFICATION
import com.argdar.blackout.AlarmAudioAttributesCreator

class Alarm(
    private val context: Context,
    private val alarmAudioAttributesCreator: AlarmAudioAttributesCreator
) {
    lateinit var alarmPlayer: MediaPlayer

    fun start() {
        val alarmUri = RingtoneManager.getDefaultUri(TYPE_ALARM) ?: RingtoneManager.getDefaultUri(TYPE_NOTIFICATION)

        alarmPlayer = MediaPlayer.create(context, alarmUri, null, alarmAudioAttributesCreator.create(), 0)
        alarmPlayer.start()
    }

    fun stop() {
        alarmPlayer.stop()
    }
}