package com.argdar.blackout.intent

import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.argdar.blackout.koin
import com.argdar.blackout.mock
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin

class PhoneCallIntentCreatorTest {

    private val subject =  PhoneCallIntentCreator()

    private val phoneNumberUri = mock<Uri>()

    @Before
    fun setUp() {
        koin {
            factory { mock<Intent>()}
        }

        mockkStatic(Uri::class)
    }

    @After
    fun tearDown() {
        stopKoin()
        unmockkAll()
    }

    @Test
    fun returnsPhoneCallIntent() {
        every {  Uri.parse("tel:123-123-1231") } returns phoneNumberUri

        val result = subject.create("123-123-1231")

        verify { result.setData(phoneNumberUri) }
        verify { result.setAction(ACTION_CALL) }
        verify { result.setFlags(FLAG_ACTIVITY_NEW_TASK) }
    }
}