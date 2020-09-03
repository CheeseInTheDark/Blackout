package com.argdar.blackout.intent

import android.content.Context
import android.content.Intent
import com.argdar.blackout.receiver.RegretReceiver

class RegrettableIntent(context: Context): Intent(context, RegretReceiver::class.java)
