package com.argdar.blackout

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.argdar.blackout.intent.PhoneCallIntentCreator
import io.mockk.every
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin

class PhoneTest {

    private lateinit var subject: Phone

    private val context = mock<Context>()
    private val phoneCallIntentCreator = mock<PhoneCallIntentCreator>()
    private val phoneCallIntent = mock<Intent>()

    private val phoneNumber = "123-123-1231"

    @Before
    fun setUp() {
        subject = Phone(context, phoneCallIntentCreator)

        every { phoneCallIntentCreator.create(phoneNumber) } returns phoneCallIntent
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun callsProvidedPhoneNumber() {
        subject.call(phoneNumber)

        verify { context.startActivity(phoneCallIntent) }
    }
}