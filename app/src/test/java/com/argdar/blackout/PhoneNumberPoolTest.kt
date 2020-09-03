package com.argdar.blackout

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog.Calls
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Data.MIMETYPE
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class PhoneNumberPoolTest {

    private lateinit var subject: PhoneNumberPool

    private val random = mock<Random>()
    private val contentResolver = mock<ContentResolver>()
    private val callLogUri = mockk<Uri>()
    private val contactsUri = mockk<Uri>()

    private lateinit var callLogCursor: Cursor
    private lateinit var contactNumbersCursor: Cursor

    private val firstCallNumber = "111-111-1111"
    private val secondCallNumber = "222-222-2222"
    private val thirdCallNumber = "333-333-3333"

    private val firstContactNumber = "101-101-1001"
    private val secondContactNumber = "202-202-2002"
    private val thirdContactNumber = "303-303-3003"

    @Before
    fun setUp() {
        mockkObject(ContentURIs)

        subject = PhoneNumberPool(random, contentResolver)
        every { ContentURIs.callLogURI } returns callLogUri
        every { ContentURIs.contactsURI } returns contactsUri

        val callHistoryNumbers = arrayOf(
            firstCallNumber,
            secondCallNumber,
            thirdCallNumber)

        val contactNumbers = arrayOf(
            firstContactNumber,
            secondContactNumber,
            thirdContactNumber
        )

        setCallHistoryNumbers(callHistoryNumbers)
        setContactNumbers(contactNumbers)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun retrievesRandomPhoneNumberFromCallHistoryWhenRandomIndexIsLowerThanHistoryCount() {
        every { random.nextInt(6) } returns 2

        val result = subject.getNumber()

        assertEquals(thirdCallNumber, result)
    }

    @Test
    fun retrievesRandomPhoneNumberFromContactsWhenRandomIndexIsHigherThanHistoryCount() {
        every { random.nextInt(6) } returns 3

        val result = subject.getNumber()

        assertEquals(firstContactNumber, result)
    }

    @Test
    fun retrievesARandomPhoneNumberNotOnTheBoundaryBetweenTheTwoLists() {
        every { random.nextInt(6) } returns 4

        val result = subject.getNumber()

        assertEquals(secondContactNumber, result)
    }

    @Test
    fun doesNotTryToUseCallHistoryRecordsIfCursorNotReturned() {
        every { random.nextInt(3) } returns 1
        every { contentResolver.query(contactsUri, any(), any(), any(), any()) } returns null

        val result = subject.getNumber()

        assertEquals(secondCallNumber, result)
    }

    @Test
    fun doesNotTryToUseContactsIfNoCursorReturned() {
        every { random.nextInt(3) } returns 2
        every { contentResolver.query(callLogUri, any(), any(), any(), any()) } returns null

        val result = subject.getNumber()

        assertEquals(thirdContactNumber, result)
    }

    @Test
    fun closesTheCursors() {
        every { random.nextInt(6) } returns 0

        subject.getNumber()

        verify { callLogCursor.close() }
        verify { contactNumbersCursor.close() }
    }

    @Test
    fun returnsNullWhenNeitherCursorIsAvailable() {
        every { contentResolver.query(contactsUri, any(), any(), any(), any()) } returns null
        every { contentResolver.query(callLogUri, any(), any(), any(), any()) } returns null

        assertNull(subject.getNumber())
    }

    private fun setCallHistoryNumbers(numbers: Array<String>) {
        callLogCursor = phoneNumberCursorWith(numbers)
        every {
            contentResolver.query(callLogUri, arrayOf(Calls.NUMBER), null, null, null)
        } returns callLogCursor
    }

    private fun setContactNumbers(contacts: Array<String>) {
        contactNumbersCursor = phoneNumberCursorWith(contacts)
        every {
            contentResolver.query(contactsUri, arrayOf(Phone.NUMBER), "$MIMETYPE = '${Phone.CONTENT_ITEM_TYPE}'", null, null)
        } returns contactNumbersCursor
    }

    private fun phoneNumberCursorWith(numbers: Array<String>): Cursor {
        return mockk {
            numbers.forEachIndexed { index, number ->
                every { move(index) } answers {
                    every { getString(0) } returns number
                    true
                }
            }

            every { count } returns numbers.size
            every { getString(more(0)) } throws IndexOutOfBoundsException()

            every { close() } answers {
                every { move(any()) } throws Exception("cursor closed")
                every { getString(any()) } throws Exception("cursor closed")
                every { isClosed } returns true
            }
        }
    }
}
