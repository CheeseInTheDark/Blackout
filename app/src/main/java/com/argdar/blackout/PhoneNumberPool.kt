package com.argdar.blackout

import android.content.ContentResolver
import android.database.Cursor
import android.provider.CallLog
import android.provider.CallLog.Calls
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import kotlin.random.Random

class PhoneNumberPool(
    private val random: Random,
    private val contentResolver: ContentResolver) {

    fun getNumber(): String? {
        val callHistoryCursor = contentResolver.query(ContentURIs.callLogURI, arrayOf(Calls.NUMBER), null, null, null)
        val contactsCursor = contentResolver.query(ContentURIs.contactsURI, arrayOf(Phone.NUMBER), "${ContactsContract.Data.MIMETYPE} = '${Phone.CONTENT_ITEM_TYPE}'", null, null)

        val historyLength = callHistoryCursor?.count ?: 0
        val contactsLength = contactsCursor?.count ?: 0

        val numberIndex = random.nextInt(historyLength + contactsLength)

        val result = if (numberIndex < historyLength) {
            getNumberFrom(callHistoryCursor, numberIndex)
        } else {
            getNumberFrom(contactsCursor, numberIndex - historyLength)
        }

        contactsCursor?.close()
        callHistoryCursor?.close()

        return result
    }

    private fun getNumberFrom(cursor: Cursor?, position: Int): String? {
        cursor?.move(position)
        return cursor?.getString(0)
    }
}