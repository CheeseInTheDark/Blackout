package com.argdar.blackout

import android.app.AlarmManager
import android.app.PendingIntent
import com.argdar.blackout.intent.PendingBlackoutIntentCreator
import io.mockk.every
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AlarmSchedulerTest {

    private lateinit var subject: AlarmScheduler

    private val alarmManager = mock<AlarmManager>()
    private val pendingBlackoutIntentCreator = mock<PendingBlackoutIntentCreator>()

    private val pendingBlackoutIntent = mock<PendingIntent>()
    private val blackoutStartTime = 10L
    private val blackoutId = 1L
    private val blackoutDuration = 200L

    @Before
    fun setUp() {
        every { pendingBlackoutIntentCreator.create(blackoutDuration, blackoutId) } returns pendingBlackoutIntent

        subject = AlarmScheduler(alarmManager, pendingBlackoutIntentCreator)
    }

    @Test
    fun schedulesGivenAlarm() {
        subject.schedule(ScheduledBlackout(blackoutId, blackoutStartTime, blackoutDuration))

        verify { alarmManager.set(AlarmManager.RTC_WAKEUP, blackoutStartTime, pendingBlackoutIntent) }
    }
}



