package com.argdar.blackout.intent

import android.app.PendingIntent
import android.content.Context
import com.argdar.blackout.koin
import com.argdar.blackout.mock
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin

class PendingRegretIntentCreatorTest {

    private lateinit var subject: PendingRegretIntentCreator

    private val context = mock<Context>()
    private val regrettableIntent = mock<RegrettableIntent>()
    private val pendingRegretIntent = mock<PendingIntent>()

    @Before
    fun setUp() {
        mockkStatic(PendingIntent::class)

        koin {
            factory { (context: Context) -> regrettableIntent }
        }

        every { PendingIntent.getBroadcast(context, 2, regrettableIntent, 0) } returns pendingRegretIntent

        subject = PendingRegretIntentCreator(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
        stopKoin()
    }

    @Test
    fun createReturnsIntentFromGetBroadcast() {
        assertEquals(pendingRegretIntent, subject.create())
    }
}