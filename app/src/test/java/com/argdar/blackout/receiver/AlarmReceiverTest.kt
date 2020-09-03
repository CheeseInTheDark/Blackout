package com.argdar.blackout.receiver

import android.content.Context
import android.content.Intent
import com.argdar.blackout.PersistentAlarmTracker
import com.argdar.blackout.intent.AlarmActivityIntentCreator
import com.argdar.blackout.intent.AlarmIntent
import com.argdar.blackout.intent.AlarmIntent.BLACKOUT_TIME_KEY
import com.argdar.blackout.koin
import com.argdar.blackout.mock
import io.mockk.every
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

class AlarmReceiverTest : KoinTest {

    private lateinit var subject: AlarmReceiver

    private val context = mock<Context>()
    private val blackoutIntent = mock<Intent>()
    private val alarmActivityIntent = mock<Intent>()
    private val alarmActivityIntentCreator = mock<AlarmActivityIntentCreator>()
    private val persistentAlarmTracker = mock<PersistentAlarmTracker>()

    @Before
    fun setUp() {
        koin {
            single { alarmActivityIntentCreator }
            single { persistentAlarmTracker }
        }

        every { blackoutIntent.getLongExtra(BLACKOUT_TIME_KEY, 0) } returns 100
        every { alarmActivityIntentCreator.create(100) } returns alarmActivityIntent
        every { blackoutIntent.getLongExtra(AlarmIntent.BLACKOUT_ID, 0) } returns 10L

        subject = AlarmReceiver()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun onReceiveLaunchesAlarmActivityWithProvidedBlackoutDuration() {
        subject.onReceive(context, blackoutIntent)

        verify { context.startActivity(alarmActivityIntent) }
    }

    @Test
    fun onReceiveRemovesAlarmRecord() {
        subject.onReceive(context, blackoutIntent)

        verify { persistentAlarmTracker.remove(10L) }
    }
}