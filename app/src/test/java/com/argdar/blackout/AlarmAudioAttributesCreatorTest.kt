package com.argdar.blackout

import android.media.AudioAttributes
import android.media.AudioAttributes.USAGE_ALARM
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Test

class AlarmAudioAttributesCreatorTest {
    @Test
    fun returnsAudioAttributesWithAlarmUsage() {
        mockkStatic(AudioAttributes::class)
        mockkConstructor(AudioAttributes.Builder::class)
        mockkConstructor(AudioAttributes::class)
        val expectedAudioAttributes = mock<AudioAttributes>()

        every { anyConstructed<AudioAttributes.Builder>().build() } returns null
        every { anyConstructed<AudioAttributes.Builder>().setUsage(USAGE_ALARM) } answers {
            every { anyConstructed<AudioAttributes.Builder>().build() } returns expectedAudioAttributes
            self as AudioAttributes.Builder?
        }

        val result = AlarmAudioAttributesCreator().create()

        assertEquals(expectedAudioAttributes, result)
    }
}