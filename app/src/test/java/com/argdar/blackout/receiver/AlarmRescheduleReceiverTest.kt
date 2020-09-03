package com.argdar.blackout.receiver

import android.content.Context
import android.content.Intent
import com.argdar.blackout.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin

class AlarmRescheduleReceiverTest {

    private lateinit var subject: AlarmRescheduleReceiver

    private val alarmScheduler = mock<AlarmScheduler>()
    private val persistentAlarmTracker = mock<PersistentAlarmTracker>()
    private val context = mock<Context>()

    private val bootIntent = mockk<Intent> {
        every { action } returns Intent.ACTION_BOOT_COMPLETED
    }

    private val someOtherIntent = mockk<Intent> {
        every { action } returns ""
    }

    private val firstBlackout = mock<ScheduledBlackout>()
    private val secondBlackout = mock<ScheduledBlackout>()

    @Before
    fun setUp() {
        koin {
            single { persistentAlarmTracker }
            single { alarmScheduler }
        }

        every { persistentAlarmTracker.getAll() } returns listOf(firstBlackout, secondBlackout)

        subject = AlarmRescheduleReceiver()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun reschedulesAllAlarmsWhenReceivingBootIntent() {
        subject.onReceive(context, bootIntent)

        verifyAll {
            alarmScheduler.schedule(firstBlackout)
            alarmScheduler.schedule(secondBlackout)
        }
    }

    @Test
    fun doesNotRescheduleIfIntentIsNotBootIntent() {
        subject.onReceive(context, someOtherIntent)

        verify(exactly = 0) { alarmScheduler.schedule(any()) }
    }
}