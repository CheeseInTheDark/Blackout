package com.argdar.blackout

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.RingtoneManager.TYPE_ALARM
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.net.Uri
import io.mockk.*
import org.junit.Before
import org.junit.Test

class AlarmTest {

    private lateinit var subject: Alarm

    private val context = mock<Context>()
    private val alarmMediaPlayer = mock<MediaPlayer>()
    private val alarmUri = mock<Uri>()
    private val notificationUri = mock<Uri>()
    private val alarmAudioAttributesCreator = mock<AlarmAudioAttributesCreator>()
    private val alarmAudioAttributes = mock<AudioAttributes>()

    @Before
    fun setUp() {
        mockkConstructor(AudioAttributes.Builder::class)
        mockkStatic(RingtoneManager::class)
        mockkStatic(MediaPlayer::class)

        every { RingtoneManager.getDefaultUri(TYPE_ALARM) } returns alarmUri
        every { RingtoneManager.getDefaultUri(TYPE_NOTIFICATION) } returns notificationUri
        every { alarmAudioAttributesCreator.create() } returns alarmAudioAttributes
        every { MediaPlayer.create(context, alarmUri, null, alarmAudioAttributes, 0) } returns alarmMediaPlayer

        subject = Alarm(context, alarmAudioAttributesCreator)
    }

    @Test
    fun playsAlarm() {
        subject.start()

        verify { alarmMediaPlayer.start() }
    }

    @Test
    fun playsAlarmUsingNotificationSoundWhenAlarmSoundIsNotSet() {
        every { RingtoneManager.getDefaultUri(TYPE_ALARM) } returns null
        every { RingtoneManager.getDefaultUri(TYPE_NOTIFICATION) } returns notificationUri
        every { MediaPlayer.create(context, alarmUri, null, alarmAudioAttributes, 0) } returns null
        every { MediaPlayer.create(context, notificationUri, null, alarmAudioAttributes, 0) } returns alarmMediaPlayer

        subject.start()

        verify { alarmMediaPlayer.start() }
    }

    @Test
    fun stopsAlarm() {
        subject.start()

        verify(exactly = 0) { alarmMediaPlayer.stop() }

        subject.stop()

        verify { alarmMediaPlayer.stop() }
    }
}