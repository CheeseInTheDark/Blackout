package com.argdar.blackout

import java.util.*

class SystemClock {
    fun currentTimeMillis() = System.currentTimeMillis()
    fun timeFromGMT() = Calendar.getInstance().timeZone.rawOffset.toLong()
}
