package com.argdar.blackout

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import com.argdar.blackout.intent.PendingRegretIntentCreator
import io.mockk.every
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test

class RegrettableConsequencesSchedulerTest {

    private lateinit var subject: RegrettableConsequencesScheduler

    private val alarmManager = mock<AlarmManager>()
    private val pendingRegretIntentCreator = mock<PendingRegretIntentCreator>()
    private val pendingRegretIntent = mock<PendingIntent>()
    private val systemClock = mock<SystemClock>()

    @Before
    fun setUp() {
        every { pendingRegretIntentCreator.create() } returns pendingRegretIntent
        every { systemClock.currentTimeMillis() } returns 1000L

        subject = RegrettableConsequencesScheduler(alarmManager, pendingRegretIntentCreator, systemClock)
    }

    @Test
    fun schedulesRussianCallettesEveryFifteenSecondsUntilTimeIsUp() {
        subject.scheduleRegrets(30000L)

        verifySequence {
            alarmManager.set(RTC_WAKEUP, 1000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 16000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 31000L, pendingRegretIntent)
        }
    }

    @Test
    fun schedulesRussianCallettesForMoreTime() {
        subject.scheduleRegrets(60000L)

        verifySequence {
            alarmManager.set(RTC_WAKEUP, 1000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 16000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 31000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 46000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 61000L, pendingRegretIntent)
        }
    }

    @Test
    fun schedulesAtLeastOneRussianCallette() {
        subject.scheduleRegrets(0L)

        verifySequence { alarmManager.set(RTC_WAKEUP, 1000L, pendingRegretIntent) }
    }

    @Test
    fun schedulesRelativeToTheCurrentTime() {
        every { systemClock.currentTimeMillis() } returns 5000L

        subject.scheduleRegrets(30000L)

        verifySequence {
            alarmManager.set(RTC_WAKEUP, 5000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 20000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 35000L, pendingRegretIntent)
        }
    }

    @Test
    fun onlyCallsTheSystemClockOnce() {
        every { systemClock.currentTimeMillis() } returns 5000L andThen 5005L

        subject.scheduleRegrets(30000L)

        verifySequence {
            alarmManager.set(RTC_WAKEUP, 5000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 20000L, pendingRegretIntent)
            alarmManager.set(RTC_WAKEUP, 35000L, pendingRegretIntent)
        }
    }
}