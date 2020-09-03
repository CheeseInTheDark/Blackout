package com.argdar.blackout

import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test

class NextTimeOccurrenceCalculatorTest {

    val day = 86400000L
    val hour = 3600000L
    val minute = 60000L
    val noon = 12 * hour
    val morning = 3 * hour
    val timezoneOffset = -4 * hour
    val startOfDay = 0L

    val systemClock = mock<SystemClock>()

    val subject = NextTimeOccurrenceCalculator(systemClock)

    @Test
    fun returnsTimeForSameDayIfTimeHasNotPassedYet() {
        every { systemClock.currentTimeMillis() } returns noon

        val result = subject.getNextTimestamp(12, 1)

        assertEquals(noon + minute, result)
    }

    @Test
    fun returnsTimeForSameDayIfTimeHasNotPassedYetForDifferentRequestedTime() {
        every { systemClock.currentTimeMillis() } returns noon

        val result = subject.getNextTimestamp(12, 2)

        assertEquals(noon + 2 * minute, result)
    }

    @Test
    fun accountsForHoursWhenCalculatingSameDayTime() {
        every { systemClock.currentTimeMillis() } returns noon

        val result = subject.getNextTimestamp(13, 0)

        assertEquals(noon + hour, result)
    }

    @Test
    fun returnsTimeForSameDayIfTimeHasNotPassedYetForDifferentCurrentTime() {
        every { systemClock.currentTimeMillis() } returns morning

        val result = subject.getNextTimestamp(12, 0)

        assertEquals(noon, result)
    }

    @Test
    fun returnsMorningTime() {
        every { systemClock.currentTimeMillis() } returns startOfDay

        val result = subject.getNextTimestamp(3, 0)

        assertEquals(morning, result)
    }

    @Test
    fun returnsNextDaysOccurrenceIfTimeHasAlreadyPassed() {
        every { systemClock.currentTimeMillis() } returns noon

        val result = subject.getNextTimestamp(3, 0)

        assertEquals(day + 3 * hour, result)
    }

    @Test
    fun addsCurrentDayToEndResult() {
        every { systemClock.currentTimeMillis() } returns day + noon

        val result = subject.getNextTimestamp(3, 0)

        assertEquals(2 * day + 3 * hour, result)
    }

    @Test
    fun onlyRetrievesTheSystemTimeOnce() {
        every { systemClock.currentTimeMillis() } returns noon andThen noon + minute

        val result = subject.getNextTimestamp(3, 0)

        assertEquals(day + 3 * hour, result)
    }

    @Test
    fun accountsForTimeZone() {
        every { systemClock.timeFromGMT() } returns timezoneOffset
        every { systemClock.currentTimeMillis() } returns noon

        val result = subject.getNextTimestamp(7, 59)

        assertEquals(day + noon - minute, result)
    }
}