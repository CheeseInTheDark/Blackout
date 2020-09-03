package com.argdar.blackout

import android.provider.CallLog
import android.provider.ContactsContract

object ContentURIs {
    val contactsURI = ContactsContract.Data.CONTENT_URI
    val callLogURI = CallLog.Calls.CONTENT_URI
}