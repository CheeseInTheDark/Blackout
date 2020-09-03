package com.argdar.blackout.intent

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.argdar.blackout.mock
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PendingBlackoutIntentCreatorTest {

    private lateinit var subject: PendingBlackoutIntentCreator

    private val context = mock<Context>()
    private val alarmIntent = mock<Intent>()
    private val pendingAlarmIntent = mock<PendingIntent>()
    private val blackoutTime = 123L
    private val blackoutId = 12L

    @Before
    fun setUp() {
        mockkStatic(PendingIntent::class)
        mockkObject(AlarmIntent)

        every { AlarmIntent.create(context, blackoutTime, blackoutId) } returns alarmIntent
        every { PendingIntent.getBroadcast(context, 1, alarmIntent, 0) } returns pendingAlarmIntent

        subject = PendingBlackoutIntentCreator(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun createReturnsIntentFromGetBroadcast() {
        assertEquals(pendingAlarmIntent, subject.create(blackoutTime, blackoutId))
    }
}