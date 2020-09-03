package com.argdar.blackout.receiver

import com.argdar.blackout.RussianCallette
import com.argdar.blackout.koin
import com.argdar.blackout.mock
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin

class RegretReceiverTest {

    private val russianCallette = mock<RussianCallette>()

    @Before
    fun setUp() {
        koin {
            single { russianCallette }
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun onReceiveActivatesRussianCallette() {
        RegretReceiver().onReceive(null, null)

        verify { russianCallette.callSomeone() }
    }
}