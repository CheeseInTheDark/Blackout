package com.argdar.blackout

import android.view.WindowManager
import com.argdar.blackout.activity.AlarmActivity
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class NotificationBarBlocker(private val windowManager: WindowManager) : KoinComponent {
    fun block(parent: AlarmActivity) {
        val notificationBlockerLayoutParams: NotificationBlockerLayoutParams by inject { parametersOf(parent) }
        val notificationBlockerViewGroup: NotificationBlockerViewGroup by inject { parametersOf(parent) }

        windowManager.addView(notificationBlockerViewGroup, notificationBlockerLayoutParams)
    }
}