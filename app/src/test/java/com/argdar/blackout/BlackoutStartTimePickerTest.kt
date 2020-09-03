package com.argdar.blackout

import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

class BlackoutStartTimePickerTest {

    val random = mock<Random>()

    val start = 500L
    val stop = 1000L
    val chosenTime = 750L
    val otherChosenTime = 850L

    val subject = BlackoutStartTimePicker(random)

    @Test
    fun returnsValueFromRandom() {
        every { random.nextLong(start, stop + 1) } returns chosenTime

        val result = subject.pickBetween(start, stop)

        assertEquals(chosenTime, result)
    }

    @Test
    fun returnsAnotherValueFromRandom() {
        every { random.nextLong(start, stop + 1) } returns otherChosenTime

        val result = subject.pickBetween(start, stop)

        assertEquals(otherChosenTime, result)
    }

    @Test
    fun returnsStartValueIfStopValueSameAsStartValue() {
        val result = subject.pickBetween(start, start)

        assertEquals(start, result)
    }

    @Test
    fun returnsStartValueIfStopValueSameLessThanStartValue() {
        val result = subject.pickBetween(start, start - 1)

        assertEquals(start, result)
    }
}