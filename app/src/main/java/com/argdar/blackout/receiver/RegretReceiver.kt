package com.argdar.blackout.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.argdar.blackout.RussianCallette
import org.koin.core.KoinComponent
import org.koin.core.get

class RegretReceiver : KoinComponent, BroadcastReceiver() {

    private val russianCallette: RussianCallette = get()

    override fun onReceive(context: Context?, intent: Intent?) {
        russianCallette.callSomeone()
    }
}
