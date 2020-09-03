package com.argdar.blackout

import io.mockk.every
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class RussianCalletteTest {

    private lateinit var subject: RussianCallette

    private val phone = mock<Phone>()
    private val numberPool = mock<PhoneNumberPool>()

    private val randomPhoneNumber = "123-456-7890"

    @Before
    fun setUp() {
        subject = RussianCallette(phone, numberPool)
    }

    @Test
    fun callsRandomKnownPhoneNumber() {
        every { numberPool.getNumber() } returns randomPhoneNumber

        subject.callSomeone()

        verify { phone.call(randomPhoneNumber) }
    }

    @Test
    fun doesNotCallPhoneNumberWhenPhoneNumberIsNull() {
        every { numberPool.getNumber() } returns null

        subject.callSomeone()

        verify(exactly = 0) { phone.call(any()) }
    }
}