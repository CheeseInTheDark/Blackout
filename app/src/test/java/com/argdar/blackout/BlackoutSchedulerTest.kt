package com.argdar.blackout

import io.mockk.every
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class BlackoutSchedulerTest {

    private val blackoutStartTimePicker = mock<BlackoutStartTimePicker>()
    private val persistentAlarmTracker = mock<PersistentAlarmTracker>()
    private val alarmScheduler = mock<AlarmScheduler>()

    private val blackoutId = 20L
    private val blackoutStartTime = 1L
    private val timeRangeBegin = 2L
    private val timeRangeEnd = 3L
    private val blackoutDuration = 4L

    private val subject = BlackoutScheduler(alarmScheduler, blackoutStartTimePicker, persistentAlarmTracker)

    @Before
    fun setUp() {
        every { persistentAlarmTracker.add(blackoutStartTime, blackoutDuration) } returns blackoutId
        every { blackoutStartTimePicker.pickBetween(timeRangeBegin, timeRangeEnd) } returns blackoutStartTime
    }

    @Test
    fun schedulesAlarmReturnedFromPicker() {
        subject.scheduleRandomBlackoutPeriod(timeRangeBegin, timeRangeEnd, blackoutDuration)

        verify { alarmScheduler.schedule(ScheduledBlackout(blackoutId, blackoutStartTime, blackoutDuration)) }
    }
}